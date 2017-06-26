package com.daimler.heybeach.backend.service;

import com.daimler.heybeach.backend.dao.OrderDao;
import com.daimler.heybeach.backend.dao.PaymentDao;
import com.daimler.heybeach.backend.dto.PaymentDto;
import com.daimler.heybeach.backend.entities.Order;
import com.daimler.heybeach.backend.entities.Payment;
import com.daimler.heybeach.backend.exception.DaoException;
import com.daimler.heybeach.backend.exception.NotFoundException;
import com.daimler.heybeach.backend.exception.PaymentException;
import com.daimler.heybeach.backend.exception.PaymentFailedException;
import com.daimler.heybeach.backend.exception.PaymentMethodNotSupportedException;
import com.daimler.heybeach.backend.exception.ValidationException;
import com.daimler.heybeach.backend.security.LoggedInUser;
import com.daimler.heybeach.data.exception.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Date;

@Service
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(HashtagService.class);

    @Autowired
    private PaymentProviderLocator paymentProviderLocator;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private PaymentDao paymentDao;

    @Autowired
    private DataSource dataSource;

    public void makePayment(Long orderId, PaymentDto paymentDto) throws PaymentFailedException, PaymentException {
        try {
            LoggedInUser user = (LoggedInUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Order order = orderDao.findByIdAndUserId(orderId, user.getUserId());
            try {
                paymentDao.findSuccessfullByOrderId(orderId);
                logger.error("Order has already successful payment");
                throw new ValidationException("Can not make payment for order with current status");
            } catch (EntityNotFoundException e) {
                //good
            }

            PaymentProvider paymentProvider = paymentProviderLocator.locate(paymentDto.getPaymentMethod());
            PaymentProvider.PaymentResult paymentResult = paymentProvider.process(order);

            Payment payment = new Payment();
            payment.setOrderId(order.getId());
            payment.setPaymentMethod(paymentDto.getPaymentMethod());
            payment.setTimestamp(new Date().getTime());
            payment.setTransaction(paymentResult.getTransactionId());
            payment.setResult(paymentResult.isSuccess());
            payment.setErrorReason(paymentResult.getErrorReason());
            paymentDao.save(payment);

            if (!paymentResult.isSuccess()) {
                throw new PaymentFailedException(paymentResult.getErrorReason());
            }

        } catch (PaymentMethodNotSupportedException e) {
            throw new ValidationException(e.getMessage());
        } catch (DaoException e) {
            throw new PaymentException(e.getMessage(), e);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("Requested order not found");
        }
    }
}
