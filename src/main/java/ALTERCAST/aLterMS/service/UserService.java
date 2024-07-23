package ALTERCAST.aLterMS.service;

import ALTERCAST.aLterMS.apiPayLoad.code.status.ErrorStatus;
import ALTERCAST.aLterMS.apiPayLoad.exception.handler.TempHandler;
import ALTERCAST.aLterMS.converter.UserConverter;
import ALTERCAST.aLterMS.domain.User;
import ALTERCAST.aLterMS.domain.UserSection;
import ALTERCAST.aLterMS.dto.UserRequestDTO;
import ALTERCAST.aLterMS.repository.UserRepository;
import ALTERCAST.aLterMS.repository.UserSectionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserSectionRepository userSectionRepository;

    @Transactional
    public User joinUser(UserRequestDTO.JoinDto request) {
        if (userRepository.findByUserId(request.getUserId()).isPresent()) {
            throw new TempHandler(ErrorStatus.ALREADY_EXIST_ID);
            // 이미 중복된 userId 존재
        }
        User user = UserConverter.toUser(request);
        return userRepository.save(user);
    }

    @Transactional
    public User getInfoOfUser(String stuId) {

        if (userRepository.findByUserId(stuId).isEmpty()) {
            throw new TempHandler(ErrorStatus.NOT_EXIST_USER);
            // 해당 학생 없음
        }

        return userRepository.findByUserId(stuId).get();
    }

    @Transactional
    public User updateInfoOfUser(String userId, UserRequestDTO.UpdateInfoDto request) {
        if (userRepository.findByUserId(userId).isEmpty()) {
            throw new TempHandler(ErrorStatus.NOT_EXIST_USER);
            // 해당 학생 없음
        }
        User user = userRepository.findByUserId(userId).get();
        user.update(request.getName(), request.getUserId(), request.getPassword(),
                request.getEmail(), request.getPhone(), request.getDeptName());

        return userRepository.save(user);
    }

    @Transactional
    public User deleteInfoOfUser(String userId) {
        if (userRepository.findByUserId(userId).isEmpty()) {
            throw new TempHandler(ErrorStatus.NOT_EXIST_USER);
            // 해당 학생 없음
        }
        User user = userRepository.findByUserId(userId).get();
        userRepository.deleteById(user.getId());

        return user;
    }

    /**
     * 해당 User의 Authority (Role 및 Privilege)의 Collection을 반환한다.
     */
    @Transactional
    public Collection<GrantedAuthority> getAuthorities(final Long id) {
        // UserSectionRepository에서 해당 User의 기록을 조회한다
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        List<UserSection> userSections = userSectionRepository.findAllByUserIdFetchJoinSection(id);
        for (UserSection usersection : userSections) {
            // 각 Role에 지정된 Privilege를 가져온다.
            List<GrantedAuthority> auths = usersection.getRole().getGrantedAuthorities().stream().toList();
            for (GrantedAuthority g : auths) {
                // 각 PRIVILEGE를 PRIVILEGE_TO_SECTION_{ID} 형태로 만들어 반환한다
                authorities.add(new GrantedAuthority() {
                    @Override
                    public String getAuthority() {
                        return g.getAuthority() + "_TO_SECTION_" + usersection.getSection().getId();
                    }
                });
            }
        }
        return authorities;
    }

}
