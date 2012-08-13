package game;

import core.common.Player;
import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import core.network.codec.ClientCodecFactory;
import core.network.MinaInput;
import log.MLogger;
import network.GlobalMessageHandler;
import registerAPI.RegisterManager;

public class MinaTimeServer {

    private static final int PORT = 12345;

    public static void main(String[] args) throws Exception {
//        DataBaseManager.doConnect();
        IoAcceptor acceptor = new NioSocketAcceptor();
//        acceptor.getFilterChain().addLast("logger", new LoggingFilter());
        acceptor.getFilterChain().addLast("codec",
                new ProtocolCodecFilter(new ClientCodecFactory()));
        MinaInput mi = new MinaInput();
        mi.setMessageHandler(new GlobalMessageHandler());
        acceptor.setHandler(mi);
        acceptor.bind(new InetSocketAddress(PORT));
        
        System.out.print("MinaTimeServer start in port:" + PORT);
//        MLogger.log("bum cheo");
//        for (int i = 10; --i >= 0;) {
//            Player p = new Player(null);
//            p.userName = "test" + i;
//            p.playerID = i;
//            RegisterManager.addUser(p);
//        }
        
//        for (int i = 10; --i >= 0;) {
//            Player p = new Player(null);
//            p.userName = "test" + i;
//            p.playerID = i;
//            RegisterManager.addUser(p);
//        }
//        
//        for (int i = 10; --i >= 0;) {
//            Player p = RegisterManager.getPlayer("test" + i);
//            System.out.println("player ID: " + p.playerID);
//            System.out.println("player Name: " + p.userName);
//        }
    }
}
