package selector_NIO;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class SocketChannelDemo01 {
	public static void main(String[] args) throws Exception {
		//0.创建选择器
		Selector selc = Selector.open();
		//1.创建SocketChannel
		SocketChannel sc = SocketChannel.open();
		//2.设定非阻塞模式
		sc.configureBlocking(false);
		//3.连接服务端
		sc.connect(new InetSocketAddress("127.0.0.1", 44444));
		sc.register(selc, SelectionKey.OP_CONNECT);

		//4.通过选择器实行选择操作
		while(true){
			selc.select();//选择器尝试选择就绪的键 选不到就阻塞 选择到就返回就绪的键的数量

			//5.得到并遍历就绪的键们
			Set<SelectionKey> keys = selc.selectedKeys();
			Iterator<SelectionKey> it = keys.iterator();
			while(it.hasNext()){
				//6.得到每一个就绪的键
				SelectionKey key = it.next();
				//7.获取就绪的键 对应的 操作 和 通道
				if(key.isAcceptable()){

				}else if(key.isConnectable()){
					//--是通道的Connect操作
					//--获取通道
					SocketChannel scx = (SocketChannel) key.channel();
					//--完成连接
					if(!scx.isConnected()){
						while(!scx.finishConnect()){};
					}
					//--将通道再次注册到selc中 关注WRITE操作
					scx.register(selc, SelectionKey.OP_WRITE);
				}else if(key.isReadable()){

				}else if(key.isWritable()){
					//--发现是Write操作就绪
					//--获取通道
					SocketChannel scx = (SocketChannel) key.channel();
					//--写出数据
					ByteBuffer buf = ByteBuffer.wrap("hello nio~ hello java~".getBytes());
					while(buf.hasRemaining()){
						scx.write(buf);
					}
					//--取消掉当前通道 在选择器中的注册 防止重复写出
					key.cancel();
				}else{
					throw new RuntimeException("未知的键,见了鬼了~");
				}
				//8.移除就绪键
				it.remove();
			}
		}
	}
}

