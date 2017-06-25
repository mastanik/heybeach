package com.daimler.heybeach.backend.service;

import com.daimler.heybeach.backend.dao.HashtagDao;
import com.daimler.heybeach.backend.dao.LikeDao;
import com.daimler.heybeach.backend.dao.PictureDao;
import com.daimler.heybeach.backend.dao.PictureHashtagDao;
import com.daimler.heybeach.backend.dto.PictureUploadDto;
import com.daimler.heybeach.backend.entities.Hashtag;
import com.daimler.heybeach.backend.entities.Like;
import com.daimler.heybeach.backend.entities.Picture;
import com.daimler.heybeach.backend.entities.PictureHashtag;
import com.daimler.heybeach.backend.exception.DaoException;
import com.daimler.heybeach.backend.exception.PictureException;
import com.daimler.heybeach.backend.exception.PictureStorageException;
import com.daimler.heybeach.backend.security.LoggedInUser;
import com.daimler.heybeach.backend.storage.PictureStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Service
public class PictureService {

    private static final Logger logger = LoggerFactory.getLogger(PictureService.class);

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
    private LikeDao likeDao;

    public void like(Long id) throws PictureException {
        try {
            LoggedInUser user = (LoggedInUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Like like = new Like(user.getUserId(), id);
            likeDao.save(like);
        } catch (DaoException e) {
            logger.error("Exception while liking a picture", e);
            throw new PictureException(e.getMessage(), e);
        }
    }

    public void approve(Long id) throws PictureException {
        try {
            LoggedInUser user = (LoggedInUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Picture picture = pictureDao.findById(id);
            picture.setApprovedBy(user.getUserId());
            picture.setPictureStatus(PictureStatus.APPROVED.getId());
            pictureDao.save(picture);
        } catch (DaoException e) {
            logger.error("Exception while approving a picture", e);
            throw new PictureException(e.getMessage(), e);
        }
    }

    public void save(PictureUploadDto pictureUpload, MultipartFile file) throws PictureException {
        try {
            LoggedInUser user = (LoggedInUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String path = pictureStorage.store(file.getOriginalFilename(), file.getBytes());
            Picture picture = new Picture();
            picture.setTimestamp(new Date().getTime());
            picture.setPath(path);
            picture.setUserId(user.getUserId());
            picture.setPictureStatus(PictureStatus.WAITING_FOR_APPROVAL.getId());
            pictureDao.save(picture);

            List<String> hashtags = pictureUpload.getHashtags();

            if (hashtags != null && !hashtags.isEmpty()) {
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
                    savedHashtags.add(hashtagDao.save(new Hashtag(newHashtag)));
                }
                hashtagService.add(hashtags);

                for (Hashtag picHashtag : savedHashtags) {
                    pictureHashtagDao.save(new PictureHashtag(picture.getId(), picHashtag.getId()));
                }
            }

        } catch (PictureStorageException | IOException | DaoException e) {
            logger.error("Exception while trying to save picture", e);
            throw new PictureException(e.getMessage(), e);
        }
    }
}
