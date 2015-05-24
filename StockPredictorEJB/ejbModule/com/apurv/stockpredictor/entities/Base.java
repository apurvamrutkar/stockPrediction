/**
 * 
 */
package com.apurv.stockpredictor.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * @author Apurv Amrutkar
 *
 */
@MappedSuperclass
public abstract class Base implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * the version field used for optimistic locking purpose for this entity
     */
    @Version
    @Column(name = "version")
    private Integer version;
	
	public Base() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	public Integer getVersion() {
		return version;
	}



	public abstract Object convertToTO();
	
	public Object convertToTOLazy(){
		return convertToTO();
	}
}
