package game;

import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import core.network.codec.ClientCodecFactory;
import core.network.MinaInput;
import network.GlobalMessageHandler;

public class MinaTimeServer {

    private static final int PORT = 12345;

    public static void main(String[] args) throws Exception {
//        DataBaseManager.doConnect();
        IoAcceptor acceptor = new NioSocketAcceptor();
        acceptor.getFilterChain().addLast("logger", new LoggingFilter());
        acceptor.getFilterChain().addLast("codec",
                new ProtocolCodecFilter(new ClientCodecFactory()));
        MinaInput mi = new MinaInput();
        mi.setMessageHandler(new GlobalMessageHandler());
        acceptor.setHandler(mi);
        acceptor.bind(new InetSocketAddress(PORT));
        System.out.print("MinaTimeServer start in port:" + PORT);
    }
}
