package ra.service.impl.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.model.entity.User;
import ra.model.entity.UserProfile;
import ra.repository.auth.UserProfileRepository;
import ra.service.UserProfileService;

@Service
public class UserProfileServiceImpl implements UserProfileService {
    @Autowired
    private UserProfileRepository userProfileRepository;

    @Override
    public void save(UserProfile userProfile) {
        userProfileRepository.save(userProfile);
    }

    @Override
    public void update(UserProfile userProfile) {
        userProfileRepository.save(userProfile);
    }

    @Override
    public void delete(Long id) {
        userProfileRepository.deleteById(id);
    }

    @Override
    public UserProfile findByUserId(Long id) {
        return userProfileRepository.findByUser_Id(id);
    }

}
