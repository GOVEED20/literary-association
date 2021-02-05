package goveed20.LiteraryAssociationApplication.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {
    @NotBlank
    public String username;

    @NotBlank
    public String password;
}
