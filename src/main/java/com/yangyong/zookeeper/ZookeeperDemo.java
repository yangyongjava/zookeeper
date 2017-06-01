package com.yangyong.zookeeper;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

public class ZookeeperDemo {
	//连接的地址和端口
	private static final String URL = "192.168.80.132:2181,192.168.80.132:2183,192.168.80.132:2183";
	private static final int TIMEOUT = 1000;
	
	public static void main(String[] args) throws Exception {
		ZooKeeper zk = null;
		zk = new ZooKeeper(URL,TIMEOUT,new Watcher(){  //线面的操作不加watch,这里一样监控不到
			//这里可以监控到整个树形结构的操作,
			//有权限的操作被监控到了，无权限的未监控到，感觉有问题********
			//也监控不到里面的操作(指linux)

			public void process(WatchedEvent event) {
				System.out.println(event.getType()+"->"+event.getState());
			}
		});
		
		//exist(zk);
//		create(zk);
//		update(zk);
//		delete(zk);
//		watcherDemo(zk);
		//aclDemo(zk);
		System.in.read();
		try {
			zk.close();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 检测节点是否存在，与watcher无关
	 * 不存在返回值为null
	 * @param zk
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	private static void exist(ZooKeeper zk) throws KeeperException, InterruptedException{
		Stat stat = zk.exists("/node_1", true);  
		System.out.println(stat);
		//System.out.println(stat.getVersion());
	}
	
	//创建节点的数据可以为空，得到也是空，
	private static void create(ZooKeeper zk) throws KeeperException, InterruptedException{
		if(zk.exists("/node_10", false)==null){
			zk.create("/node_10", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			System.out.println(zk.getData("/node_10", true,null ));
		}
	}
	
	private static void update(ZooKeeper zk) throws KeeperException, InterruptedException{
		if(zk.exists("/node_4", false)!=null){   
			zk.setData("/node_4", "xx".getBytes(), -1);  //-1代表不进行更改
			zk.setData("/node_4", "yy".getBytes(), -1);  //-1代表不进行更改
		}
	}
	
	//不能进行级联删除
	private static void delete(ZooKeeper zk) throws KeeperException, InterruptedException{
		if(zk.exists("/node_4", false)!=null){
			zk.delete("/node_4", -1);
		}
	}
	
	//不能进行级联删除
	private static void acl(ZooKeeper zk) throws KeeperException, InterruptedException{
			if(zk.exists("/node_4", false)!=null){
				zk.delete("/node_4", -1);
			}
	}
	
	
	 private static void watcherDemo(final ZooKeeper zk) throws KeeperException, InterruptedException {
	        if(zk.exists("/node_4",true)==null) {
	            zk.create("/node_4", "abc".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
	        }
	        //这里只能监控到一次改变  后面的监控不到了
	        byte[] rsByt = zk.getData("/node_4", new Watcher() {
	            public void process(WatchedEvent event) {
	                System.out.println("触发节点事件：" + event.getPath());
	                try {
	                    System.out.println("HHHH"+new String(zk.getData(event.getPath(),true,null)));
	                } catch (KeeperException e) {
	                    e.printStackTrace();
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
	            }
	        }, null);
	        System.out.println(new String(rsByt));
	        zk.setData("/node_4","pksyz".getBytes(),-1);
	        System.out.println(new String(zk.getData("/node_4",true,null)));
	        zk.setData("/node_4","pksyyyyyz".getBytes(),-1);
	    }
	 
	 private static void aclDemo(ZooKeeper zk) throws KeeperException, InterruptedException, NoSuchAlgorithmException {
	        if(zk.exists("/node_11",true)==null){
	            ACL acl=new ACL(ZooDefs.Perms.ALL,new Id("digest", DigestAuthenticationProvider.generateDigest("root:root")));
	            List<ACL> acls=new ArrayList<ACL>();
	            acls.add(acl);
	            zk.create("/node_11","abc".getBytes(), acls, CreateMode.PERSISTENT);
	            System.out.println(new String(zk.getData("/node_3",true,null)));
	        }
	        zk.addAuthInfo("digest","root:root".getBytes());
	        System.out.println(zk.getData("/node_11",false,null));  //getData得到的是节点的数据 data
	    }
	
	
}