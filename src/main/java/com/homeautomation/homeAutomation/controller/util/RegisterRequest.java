    package com.homeautomation.homeAutomation.controller.util;

    import com.homeautomation.homeAutomation.config.ValidationGroups;
    import jakarta.validation.constraints.NotBlank;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    import java.util.Set;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    //Like A DTO

    public class RegisterRequest {
        @NotBlank(message = "Username cannot be blank", groups = ValidationGroups.Create.class)
        private String username;

        @NotBlank(message = "Password cannot be blank", groups = ValidationGroups.Create.class)
        private String password;
    //    private Set<String> roles; //actually not needed, duh
    }
