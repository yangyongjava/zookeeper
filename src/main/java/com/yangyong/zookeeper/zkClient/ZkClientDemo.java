package com.yangyong.zookeeper.zkClient;


import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.KeeperException;

public class ZkClientDemo {
	//连接的地址和端口
	private static final String URL = "192.168.80.132:2181,192.168.80.132:2183,192.168.80.132:2183";
	private static final int TIMEOUT = 1000;
	
	public static void main(String[] args) throws Exception {
		ZkClient zk  = new ZkClient(URL,TIMEOUT,TIMEOUT);
		//exist(zk);
		//create(zk);
		//delete(zk);
		sub(zk);
		System.in.read();
		zk.close();
	}
	
	private static void exist(ZkClient zk) throws KeeperException, InterruptedException{
	   System.out.println(zk.exists("/node_155"));  //返回true 或false
	}
	
	//创建节点的数据可以为空，得到也是空，
	private static void create(ZkClient zk) throws KeeperException, InterruptedException{
		if(!zk.exists("/test/test/test")){
			//1.这种方式不行
			//zk.create("/test/test/test", "44444", CreateMode.PERSISTENT);
			zk.createPersistent("/test/test", true);  //这种方式能进行递归创建
		}
	}
	
	private static void update(ZkClient zk){
        zk.writeData("/node_11","zyz");
    }

    private static void delete(ZkClient zk){
        boolean bool=zk.deleteRecursive("/test");  //可以级联杀出
        System.out.println(bool);
    }
    
    private static void sub(ZkClient zk){
    	
    	//该方法是订阅当前path下子节点的增加、删除事件
//    	zk.subscribeChildChanges("/test",new IZkChildListener() {
//			
//			public void handleChildChange(String arg0, List<String> currentChilds)
//					throws Exception {
//				for(String str:currentChilds){
//                    System.out.println(str);
//                }
//			}
//		});
    	
    	//触发指定path数据修改、删除事件  (有待考虑)
    	zk.subscribeDataChanges("/test", new IZkDataListener() {
			
			public void handleDataDeleted(String arg0) throws Exception {
				System.out.println(arg0);
			}
			
			public void handleDataChange(String arg0, Object arg1) throws Exception {
				System.out.println(arg0+"<>"+arg1);
			}
		});
    	//zk.delete("/test/test");
    	
//    	zk.subscribeStateChanges(new IZkStateListener() {
//			
//			public void handleStateChanged(KeeperState arg0) throws Exception {
//				System.out.println(arg0+"handleStateChanged");
//			}
//			
//			public void handleNewSession() throws Exception {
//				System.out.println("handleNewSession");
//			}
//		});
    }	
}