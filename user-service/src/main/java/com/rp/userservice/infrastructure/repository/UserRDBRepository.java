package com.rp.userservice.infrastructure.repository;

import com.rp.userservice.adapter.UserAdapter;
import com.rp.userservice.domain.exception.UserNotFoundException;
import com.rp.userservice.domain.model.User;
import com.rp.userservice.domain.port.UserPort;
import com.rp.userservice.infrastructure.repository.dao.UserEntityDao;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class UserRDBRepository implements UserPort {

    private final UserEntityDao userEntityDao;
    private final UserAdapter userAdapter;

    public UserRDBRepository(UserEntityDao userEntityDao,
                             UserAdapter userAdapter) {
        this.userEntityDao = userEntityDao;
        this.userAdapter = userAdapter;
    }

    @Override
    public Flux<User> findAll() {
        return this.userEntityDao.findAll()
                .map(this.userAdapter::toModel);
    }

    @Override
    public Mono<User> findById(long id) {
        return this.userEntityDao.findById(id)
                .switchIfEmpty(Mono.error(() -> new UserNotFoundException(id)))
                .map(this.userAdapter::toModel);

    }

    @Override
    public Mono<User> save(User user) {
        return Mono.just(user)
                .map(this.userAdapter::toEntity)
                .flatMap(this.userEntityDao::save)
                .map(this.userAdapter::toModel);
    }

    @Override
    public Mono<User> update(User user) {
        return Mono.just(user)
                .filterWhen(usr -> this.exists(usr.id()))
                .switchIfEmpty(Mono.error(() -> new UserNotFoundException(user.id())))
                .flatMap(this::save);

    }

    @Override
    public Mono<Void> deleteById(long id) {
        return Mono.just(id)
                .filterWhen(this::exists)
                .switchIfEmpty(Mono.error(() -> new UserNotFoundException(id)))
                .flatMap(this.userEntityDao::deleteById);
    }

    @Override
    public Mono<Boolean> updateBalance(long userId, int amount) {
        return this.userEntityDao.updateBalance(userId, amount);
    }

    @Override
    public Mono<Boolean> exists(long userId) {
        return this.userEntityDao.existsById(userId);
    }

}
