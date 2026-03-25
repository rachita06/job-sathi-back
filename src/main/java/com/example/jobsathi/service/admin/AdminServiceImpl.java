package com.example.jobsathi.service.admin;

import com.example.jobsathi.dto.response.UserResponseDTO;
import com.example.jobsathi.repository.RegisterRepository;
import com.example.jobsathi.util.ResponseWrapperDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Rabindra Adhikari on 3/25/26
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AdminServiceImpl implements AdminService {
    private final RegisterRepository registerRepository;

    @Override
    public ResponseWrapperDTO<List<UserResponseDTO>> getUsers() {
        Instant start = Instant.now();
        ResponseWrapperDTO<List<UserResponseDTO>> responseWrapperDTO = new ResponseWrapperDTO<>();
        var response = registerRepository.findAll()
                .stream()
                .map(user -> new UserResponseDTO(user.getRole(), user.getEmail()))
                .collect(Collectors.toList());
        responseWrapperDTO.setData(response);
        responseWrapperDTO.setSummary(new ResponseWrapperDTO.Summary(response.size()));
        LOGGER.info("Total time taken to fetch user is {}", Duration.between(start, Instant.now()).toMillis());
        return responseWrapperDTO;
    }
}
