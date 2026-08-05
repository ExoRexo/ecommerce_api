package alexo.ecommerce_api.entity.identity;

import alexo.ecommerce_api.entity.common.EnumCodeMapper;
import alexo.ecommerce_api.entity.enums.RoleCode;
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
 * Security role dictionary.
 */
@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 100)
    private String code;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    /**
     * @return code converted to role enum.
     */
    @Transient
    public RoleCode getRoleCodeEnum() {
        return EnumCodeMapper.fromCode(RoleCode.class, code);
    }

    /**
     * Sets role code from enum value.
     */
    public void setRoleCodeEnum(RoleCode codeEnum) {
        this.code = codeEnum == null ? null : codeEnum.getCode();
    }
}
