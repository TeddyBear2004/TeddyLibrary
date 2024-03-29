package de.teddy.bansystem.tables;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@Table(name = "bansystem_player")
public class BansystemPlayer implements Serializable {

	@Serial private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "uuid", nullable = false, unique = true, length = 36, columnDefinition = "varchar(36)")
	private String uuid;

	@Column(name = "username")
	private String username;

	@Column(name = "first_login")
	private Date firstLogin;

	@Column(name = "last_login")
	private Date lastLogin;

	@Column(name = "cam_owner_uuid", length = 36, columnDefinition = "varchar(36)")
	private String camOwnerUuid;

	public BansystemPlayer(){}

	public BansystemPlayer(String uuid, Date firstLogin){
		this.uuid = uuid;
		this.firstLogin = firstLogin;
	}

	@Override
	public boolean equals(Object o){
		if(this == o)
			return true;
		if(o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
			return false;
		BansystemPlayer that = (BansystemPlayer)o;
		return uuid != null && Objects.equals(uuid, that.uuid);
	}

	@Override
	public int hashCode(){
		return getClass().hashCode();
	}
}