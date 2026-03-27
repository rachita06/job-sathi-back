package com.example.jobsathi.service.admin;

import com.example.jobsathi.dto.response.UserResponseDTO;
import com.example.jobsathi.entity.Register;
import com.example.jobsathi.repository.RegisterRepository;
import com.example.jobsathi.util.ResponseWrapperDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.loomchild.segment.util.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @Override
    public ResponseWrapperDTO<UserResponseDTO> getUser(String email) {
        Instant start = Instant.now();
        LOGGER.info("Fetching user profile for email: {}", email);
        Register user = registerRepository.findByEmail(email);

        if (user==null){
            throw new UsernameNotFoundException("User not found");
        }

        UserResponseDTO response = new UserResponseDTO(user.getRole(), user.getEmail());
        ResponseWrapperDTO<UserResponseDTO> responseWrapperDTO = new ResponseWrapperDTO<>();
        responseWrapperDTO.setData(response);
        responseWrapperDTO.setSummary(new ResponseWrapperDTO.Summary(1));

        LOGGER.info("Successfully fetched user for email: {} | Time taken: {} ms", email, Duration.between(start, Instant.now()).toMillis());
        return responseWrapperDTO;
    }
}
