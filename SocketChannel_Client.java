package test01;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SocketChannel_Client {
	public static void main(String[] args) {
		try {
			//1.获取socketChannel对象
			SocketChannel socketChannel=SocketChannel.open();
			//2.设置socketchannel为非阻塞
			socketChannel.configureBlocking(false);
			//3.连接
			boolean isCon=socketChannel.connect(new InetSocketAddress("127.0.0.1",9991));
		    while(!isCon){
		    	isCon=socketChannel.finishConnect();
		    }
		    //4.读写操作
		    ByteBuffer buf=ByteBuffer.wrap("hello nio!".getBytes());
			while(buf.hasRemaining()){
				socketChannel.write(buf);
			}
			//5.关闭
			socketChannel.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
