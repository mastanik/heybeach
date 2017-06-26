package com.daimler.heybeach.backend.service;

import com.daimler.heybeach.backend.dao.HashtagDao;
import com.daimler.heybeach.backend.dao.LikeDao;
import com.daimler.heybeach.backend.dao.PictureDao;
import com.daimler.heybeach.backend.dao.PictureHashtagDao;
import com.daimler.heybeach.backend.dao.PictureStatusLogDao;
import com.daimler.heybeach.backend.dto.ApproveDto;
import com.daimler.heybeach.backend.dto.PictureUploadDto;
import com.daimler.heybeach.backend.entities.Hashtag;
import com.daimler.heybeach.backend.entities.Like;
import com.daimler.heybeach.backend.entities.Picture;
import com.daimler.heybeach.backend.entities.PictureHashtag;
import com.daimler.heybeach.backend.entities.PictureStatusLog;
import com.daimler.heybeach.backend.exception.DaoException;
import com.daimler.heybeach.backend.exception.NotFoundException;
import com.daimler.heybeach.backend.exception.PictureException;
import com.daimler.heybeach.backend.exception.PictureStorageException;
import com.daimler.heybeach.backend.exception.ServiceException;
import com.daimler.heybeach.backend.exception.ValidationException;
import com.daimler.heybeach.backend.security.LoggedInUser;
import com.daimler.heybeach.backend.storage.PictureStorage;
import com.daimler.heybeach.backend.transaction.TransactionalOperation;
import com.daimler.heybeach.data.exception.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Service
public class PictureService {

    private static final Logger logger = LoggerFactory.getLogger(PictureService.class);

    @Value("${marketplace.entrance.like.amount}")
    private Integer marketplaceEntranceLikeAmount;

    @Autowired
    private PictureStorage pictureStorage;

    @Autowired
    private HashtagDao hashtagDao;

    @Autowired
    private PictureDao pictureDao;

    @Autowired
    private PictureHashtagDao pictureHashtagDao;

    @Autowired
    private HashtagService hashtagService;

    @Autowired
    private PictureStatusLogDao pictureStatusLogDao;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private LikeDao likeDao;

    public void like(Long id) throws PictureException {
        try {
            new TransactionalOperation() {
                @Override
                public void execute(Connection con) throws PictureException, DaoException {
                    try {
                        LoggedInUser user = (LoggedInUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                        Like like = new Like(user.getUserId(), id);

                        Picture picture = pictureDao.findById(id);
                        likeDao.save(like, con);

                        Long likeCount = likeDao.countByPictureId(id);
                        if (likeCount >= marketplaceEntranceLikeAmount) {

                            if (PictureStatus.getById(picture.getPictureStatus()) == PictureStatus.APPROVED) {
                                picture.setPictureStatus(PictureStatus.READY_FOR_MARKETPLACE.getId());
                                pictureDao.save(picture, con);

                                PictureStatusLog pictureStatusLog = new PictureStatusLog();
                                pictureStatusLog.setStatusId(PictureStatus.READY_FOR_MARKETPLACE.getId());
                                pictureStatusLog.setTimestamp(new Date().getTime());
                                pictureStatusLog.setComment("Reached " + marketplaceEntranceLikeAmount + " likes");
                                pictureStatusLog.setPictureId(picture.getId());
                                pictureStatusLogDao.save(pictureStatusLog, con);
                            }
                        }
                    } catch (EntityNotFoundException e) {
                        throw new NotFoundException("Requested picture not found");
                    }
                }
            }.executeInTransaction(dataSource);
        } catch (DaoException | ServiceException e) {
            logger.error("Exception while liking a picture", e);
            throw new PictureException(e.getMessage(), e);
        }
    }

    public void disapprove(Long id, ApproveDto dto) throws PictureException {
        try {
            new TransactionalOperation() {
                @Override
                public void execute(Connection con) throws PictureException, DaoException {
                    try {
                        LoggedInUser user = (LoggedInUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                        Picture picture = pictureDao.findById(id);
                        PictureStatus pictureStatus = PictureStatus.getById(picture.getPictureStatus());

                        if (pictureStatus != PictureStatus.READY_FOR_MARKETPLACE) {
                            throw new ValidationException("Picture can not be disapproved with current status");
                        }

                        picture.setPictureStatus(PictureStatus.DISAPPROVED_FROM_MARKETPLACE.getId());
                        pictureDao.save(picture, con);
                        createPictureStatusLog(id, user.getUserId(), dto.getComment(), pictureStatus, con);
                    } catch (EntityNotFoundException e) {
                        throw new NotFoundException("Requested picture not found");
                    }
                }
            }.executeInTransaction(dataSource);
        } catch (DaoException | ServiceException e) {
            logger.error("Exception while approving a picture", e);
            throw new PictureException(e.getMessage(), e);
        }
    }

    public void approve(Long id, ApproveDto dto) throws PictureException {
        try {
            new TransactionalOperation() {
                @Override
                public void execute(Connection con) throws PictureException, DaoException {
                    try {
                        LoggedInUser user = (LoggedInUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                        Picture picture = pictureDao.findById(id);
                        PictureStatus pictureStatus = PictureStatus.getById(picture.getPictureStatus());
                        PictureStatus pictureNextStatus;

                        if (pictureStatus == PictureStatus.WAITING_FOR_APPROVAL) {
                            pictureNextStatus = PictureStatus.APPROVED;
                        } else if (pictureStatus == PictureStatus.READY_FOR_MARKETPLACE) {
                            pictureNextStatus = PictureStatus.AVAILABLE_IN_MARKETPLACE;
                        } else {
                            throw new PictureException("Picture can not be approved with current status");
                        }

                        picture.setPictureStatus(pictureNextStatus.getId());
                        pictureDao.save(picture, con);
                        createPictureStatusLog(id, user.getUserId(), dto.getComment(), pictureNextStatus, con);

                    } catch (EntityNotFoundException e) {
                        throw new NotFoundException("Requested picture not found");
                    }
                }
            }.executeInTransaction(dataSource);
        } catch (DaoException | ServiceException e) {
            logger.error("Exception while approving a picture", e);
            throw new PictureException(e.getMessage(), e);
        }
    }

    private void createPictureStatusLog(Long pictureId, Long userId, String comment, PictureStatus pictureStatus, Connection con) throws DaoException, ValidationException {
        PictureStatusLog pictureStatusLog = new PictureStatusLog();
        pictureStatusLog.setApprovedBy(userId);
        pictureStatusLog.setStatusId(pictureStatus.getId());
        pictureStatusLog.setTimestamp(new Date().getTime());
        pictureStatusLog.setComment(comment);
        pictureStatusLog.setPictureId(pictureId);
        pictureStatusLogDao.save(pictureStatusLog, con);
    }

    public void save(PictureUploadDto pictureUpload, MultipartFile file) throws PictureException, ValidationException {
        try {
            new TransactionalOperation() {
                @Override
                public void execute(Connection con) throws PictureException, DaoException {

                    try {
                        LoggedInUser user = (LoggedInUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                        String path = pictureStorage.store(file.getOriginalFilename(), file.getBytes());
                        Picture picture = new Picture();
                        picture.setTimestamp(new Date().getTime());
                        picture.setPath(path);
                        picture.setUserId(user.getUserId());
                        picture.setPictureStatus(PictureStatus.WAITING_FOR_APPROVAL.getId());
                        picture.setTitle(pictureUpload.getTitle());
                        picture.setDescription(pictureUpload.getDescription());
                        picture.setPrice(pictureUpload.getPrice());
                        pictureDao.save(picture, con);

                        List<String> hashtags = pictureUpload.getHashtags();

                        if (hashtags != null && !hashtags.isEmpty()) {
                            hashtagService.add(hashtags);

                            List<Hashtag> savedHashtags = new LinkedList<>();
                            Iterator<String> it = hashtags.iterator();
                            while (it.hasNext()) {
                                Hashtag savedHashtag = hashtagDao.findByName(it.next());
                                if (savedHashtag != null) {
                                    savedHashtags.add(savedHashtag);
                                    it.remove();
                                }
                            }
                            for (String newHashtag : hashtags) {
                                savedHashtags.add(hashtagDao.save(new Hashtag(newHashtag), con));
                            }

                            for (Hashtag picHashtag : savedHashtags) {
                                pictureHashtagDao.save(new PictureHashtag(picture.getId(), picHashtag.getId()), con);
                            }
                        }
                    } catch (IOException | PictureStorageException e) {
                        logger.error("Exception while trying to save picture", e);
                        throw new PictureException(e.getMessage(), e);
                    }
                }
            }.executeInTransaction(dataSource);
        } catch (DaoException | ServiceException e) {
            logger.error("Exception while trying to save picture", e);
            throw new PictureException(e.getMessage(), e);
        }
    }

    public void remove(Long id) throws PictureException {
        try {
            LoggedInUser user = (LoggedInUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Picture picture = pictureDao.findByIdAndUserId(id, user.getUserId());
            if (picture != null) {
                pictureDao.remove(picture);
            }
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("Requested picture not found");
        } catch (DaoException e) {
            throw new PictureException(e.getMessage(), e);
        }
    }
}
