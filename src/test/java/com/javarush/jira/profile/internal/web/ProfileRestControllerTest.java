package com.javarush.jira.profile.internal.web;

import com.javarush.jira.AbstractControllerTest;
import com.javarush.jira.common.util.JsonUtil;
import com.javarush.jira.profile.ProfileTo;
import com.javarush.jira.profile.internal.ProfileMapper;
import com.javarush.jira.profile.internal.ProfileRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.javarush.jira.login.internal.web.UserTestData.*;
import static com.javarush.jira.profile.internal.web.ProfileTestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProfileRestControllerTest extends AbstractControllerTest {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    ProfileMapper mapper;

    //Tests get method without authorization will return a 401 Unauthorized status.
    @Test
    public void testGetWhen401ThenReturn401() throws Exception {
        perform(MockMvcRequestBuilders.get(ProfileRestController.REST_URL))
                .andExpect(status().isUnauthorized());
    }

    //Tests update method without authorization will return a 401 Unauthorized status.
    @Test
    public void testUpdateWhen401ThenReturn401() throws Exception {
        perform(MockMvcRequestBuilders.put(ProfileRestController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    //Tests get method that returns a user profile. Run the test on behalf of the user with the
    //specified email (USER_MAIL). Verifies that the response has a 200 OK status, contains a JSON
    //representation of the user profile, and matches the expected USER_PROFILE_TO profile.
    @Test
    @WithUserDetails(value = USER_MAIL)
    public void testGetWhenAuthorizedAsUserThenReturnUserProfile() throws Exception {
        perform(MockMvcRequestBuilders.get(ProfileRestController.REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(PROFILE_TO_MATCHER.contentJson(USER_PROFILE_TO));
    }

    //Everything is similar to the "testGetWhenAuthorizedAsUserThenReturnUserProfile()", only now trying the admin.
    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    public void testGetWhenAuthorizedAsAdminThenReturnAdminProfile() throws Exception {
        perform(MockMvcRequestBuilders.get(ProfileRestController.REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(PROFILE_TO_MATCHER.contentJson(ADMIN_PROFILE_TO));
    }

    //Checking the user profile update. Run the test on behalf of the user with the specified email
    //(USER_MAIL). PUT request to a URL with an updated profile in JSON format. Check that the response
    //has a status of 422 Unprocessable Entity. This means that the server rejected the request
    //due to incorrect data (in this case, an incorrect profile ID). If the server returns the expected
    //status, then the test will be considered passed.
    @Test
    @WithUserDetails(value = USER_MAIL)
    public void testUpdateWhenIncorrectIdThenReturn422() throws Exception {
        ProfileTo updatedProfileTo = getUpdatedTo(ADMIN_ID);

        perform(MockMvcRequestBuilders.put(ProfileRestController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedProfileTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    //Testing updating an existing user profile. Perform a PUT request to the URL, passing the updated
    //profile in JSON format. Check that the response has a status of 204 No Content, which means
    //the profile update was successful. After this, check that the profile was successfully updated
    //in the database.
    @Test
    @WithUserDetails(value = USER_MAIL)
    public void testUpdateWhenExistingValidProfileThenSaveToDB() throws Exception {
        ProfileTo updatedProfileTo = getUpdatedTo(USER_ID);

        perform(MockMvcRequestBuilders.put(ProfileRestController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedProfileTo)))
                .andDo(print())
                .andExpect(status().isNoContent());

        ProfileTo afterUpdateProfileTo = mapper.toTo(profileRepository.getExisted(USER_ID));

        assertAll(
          () -> assertEquals(afterUpdateProfileTo.id(), updatedProfileTo.id()),
          () -> assertThat(afterUpdateProfileTo.getContacts())
                  .hasSameElementsAs(updatedProfileTo.getContacts()),
          () -> assertThat(afterUpdateProfileTo.getMailNotifications())
                  .hasSameElementsAs(updatedProfileTo.getMailNotifications())
        );
    }
}