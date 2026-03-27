package com.example.jobsathi.repository;

import com.example.jobsathi.entity.Register;
import io.grpc.netty.shaded.io.netty.handler.codec.http2.Http2Connection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created by Rabindra Adhikari on 2/11/26
 */
public interface RegisterRepository extends JpaRepository<Register, Long> {
    boolean existsByEmail(String email);
    Optional<Register> getByEmail(String email);

    Register findByEmail(String username);
}
