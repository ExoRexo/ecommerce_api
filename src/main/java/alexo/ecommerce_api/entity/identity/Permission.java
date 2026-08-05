package alexo.ecommerce_api.entity.identity;

import alexo.ecommerce_api.entity.common.EnumCodeMapper;
import alexo.ecommerce_api.entity.enums.PermissionCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Security permission dictionary.
 */
@Entity
@Table(name = "permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String code;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    /**
     * @return code converted to permission enum.
     */
    @Transient
    public PermissionCode getPermissionCodeEnum() {
        return EnumCodeMapper.fromCode(PermissionCode.class, code);
    }

    /**
     * Sets permission code from enum value.
     */
    public void setPermissionCodeEnum(PermissionCode codeEnum) {
        this.code = codeEnum == null ? null : codeEnum.getCode();
    }
}
