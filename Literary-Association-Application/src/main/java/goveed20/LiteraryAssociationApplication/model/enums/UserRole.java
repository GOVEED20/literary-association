package goveed20.LiteraryAssociationApplication.model.enums;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    READER,
    WRITER,
    LECTOR,
    BOARD_MEMBER,
    EDITOR;

    @Override
    public String getAuthority() {
        return name();
    }
}
