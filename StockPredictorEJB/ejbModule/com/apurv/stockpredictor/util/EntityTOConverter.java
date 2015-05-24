/**
 * 
 */
package com.apurv.stockpredictor.util;

import java.util.ArrayList;
import java.util.List;

import com.apurv.stockpredictor.entities.Base;

/**
 * @author Apurv Amrutkar
 *
 */
public class EntityTOConverter<E extends Base, T extends Object> {
	
	public List<T> convertToTOList(List<E> entryList){
		if(entryList==null){
			return null;
		}
		List<T> output = new ArrayList<>();
		for(E e : entryList){
			output.add((T)e.convertToTO());
		}
		return output;
	}
	
	public List<T> convertToTOListLazy(List<E> entryList){
		if(entryList==null){
			return null;
		}
		List<T> output = new ArrayList<>();
		for(E e : entryList){
			output.add((T)e.convertToTOLazy());
		}
		return output;
	}

}
