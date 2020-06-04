package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AdministratorDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import com.upgrad.quora.service.type.ActionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AdministratorDao administratorDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public String deleteUser(final String uuid, final String authorization)throws AuthorizationFailedException, UserNotFoundException{
        UserAuthTokenEntity userAuthTokenEntity = userDao.isValidActiveAuthToken(authorization, ActionType.DELETE_USER);

        if (userDao.hasUserSignedIn(authorization)) {
            if (userAuthTokenEntity != null) {
                if (userDao.isRoleAdmin(authorization)) {
                    UserEntity userEntity = userDao.getUserById(uuid);
                    if (userEntity == null) {
                        throw new UserNotFoundException("USR-001", "User with the entered Uuid to be deleted does not exist");
                    } else {
                        return administratorDao.deleteUser(uuid);
                    }
                } else {
                    throw new AuthorizationFailedException("ATHR-003", "Unauthorized Access, Entered user is not an admin");
                }
            } else {
                throw new AuthorizationFailedException("ATHR-002", "User is signed out");
            }
        } else {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
    }
}

