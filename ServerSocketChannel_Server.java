package test01;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class ServerSocketChannel_Server {
	public static void main(String[] args) {
		try {
			//1.打开ServerSocketChannel通道
			ServerSocketChannel serverSocketChannel=ServerSocketChannel.open();
			//2.默认是阻塞状态,设置成false就变成非阻塞
			serverSocketChannel.configureBlocking(false);
			//3.监听端口
			//serverSocketChannel.bind(new InetSocketAddress(9991));
			//以前的做法,先必须获取到ServerSocket对象,然后才能监听端口
			ServerSocket ss=serverSocketChannel.socket();
			ss.bind(new InetSocketAddress(9991));
		    //4.接收客户端的连接,注意一个问题,accept是非阻塞方法
			SocketChannel socketChannel=null;
			while(socketChannel==null){
				socketChannel=serverSocketChannel.accept();
			}
			//5.给接收到的客户端的连接对象为非阻塞状态		
			socketChannel.configureBlocking(false);
			//6.开始利用socketChannel对象做读写操作
			//创建一个缓冲的对象
			ByteBuffer buf=ByteBuffer.allocate(10);
			//6.1读取客户端的数据
			while(buf.hasRemaining()){
				socketChannel.read(buf);
			}
			//6.2把读取到的数据输出
			byte[] bs=buf.array();
			System.out.println(new  String(bs));
			//7.关闭资源
			socketChannel.close();
			serverSocketChannel.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
