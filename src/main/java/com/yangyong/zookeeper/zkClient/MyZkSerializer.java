package com.yangyong.zookeeper.zkClient;

import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;

import java.io.UnsupportedEncodingException;

public class MyZkSerializer implements ZkSerializer{

	public Object deserialize(byte[] arg0) throws ZkMarshallingError {
		try {
			return new String(arg0, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public byte[] serialize(Object arg0) throws ZkMarshallingError {
		try {
			return String.valueOf(arg0).getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
}
