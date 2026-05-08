package ra.service;

import ra.model.entity.User;
import ra.model.entity.UserProfile;

public interface UserProfileService {
    void save(UserProfile userProfile);
    void update(UserProfile userProfile);
    void delete(Long id);
    UserProfile findByUserId(Long id);
}
