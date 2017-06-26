package com.daimler.heybeach.backend.service;

import com.daimler.heybeach.backend.dao.OrderDao;
import com.daimler.heybeach.backend.dao.OrderItemDao;
import com.daimler.heybeach.backend.dao.PictureDao;
import com.daimler.heybeach.backend.dto.OrderDto;
import com.daimler.heybeach.backend.entities.Order;
import com.daimler.heybeach.backend.entities.OrderItem;
import com.daimler.heybeach.backend.entities.Picture;
import com.daimler.heybeach.backend.exception.DaoException;
import com.daimler.heybeach.backend.exception.NotFoundException;
import com.daimler.heybeach.backend.exception.OrderException;
import com.daimler.heybeach.backend.exception.ValidationException;
import com.daimler.heybeach.backend.security.LoggedInUser;
import com.daimler.heybeach.data.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private PictureDao pictureDao;

    @Autowired
    private OrderItemDao orderItemDao;

    public void purchase(OrderDto dto) throws NotFoundException, OrderException, ValidationException {

        try {
            LoggedInUser user = (LoggedInUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            List<Picture> pictures = new LinkedList<>();
            BigDecimal amount = new BigDecimal(0);
            for (Long pictureId : dto.getPictureIds()) {
                Picture picture = pictureDao.findById(pictureId);
                PictureStatus status = PictureStatus.getById(picture.getPictureStatus());
                if (status != PictureStatus.AVAILABLE_IN_MARKETPLACE) {
                    throw new ValidationException("One of the items is not available");
                }
                pictures.add(picture);
                amount = amount.add(picture.getPrice()).setScale(2, RoundingMode.HALF_EVEN);
            }

            Order order = new Order();
            order.setBillingAddress(dto.getBillingAddress());
            order.setFullName(dto.getFullName());
            order.setShippingAddress(dto.getShippingAddress());
            order.setTimestamp(new Date().getTime());
            order.setUserId(user.getUserId());
            order.setAmount(amount);
            orderDao.save(order);

            for (Picture picture : pictures) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrderId(order.getId());
                orderItem.setPictureId(picture.getId());
                orderItem.setPrice(picture.getPrice().setScale(2, RoundingMode.HALF_EVEN));
                orderItem.setTitle(picture.getTitle());
                orderItemDao.save(orderItem);
            }

        } catch (DaoException e) {
            throw new OrderException(e.getMessage(), e);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("Requested picture not found");
        }
    }
}
