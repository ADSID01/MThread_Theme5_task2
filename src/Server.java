import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress("localhost", 23444));

        while (true) {
            try (SocketChannel socketChannel = serverChannel.accept()) {
                final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);

                while (socketChannel.isConnected()) {
                    int bytesCount = socketChannel.read(inputBuffer);
                    if (bytesCount == -1)
                        break;
                    final String clientMsg = new String(inputBuffer.array(), 0,
                            bytesCount, StandardCharsets.UTF_8);
                    inputBuffer.clear();
                    System.out.println("Client: " + clientMsg);
                    socketChannel.write(ByteBuffer.wrap(
                            ("Текст без пробелов: " + getStringNoGaps(clientMsg)).getBytes(StandardCharsets.UTF_8)));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String getStringNoGaps(String txt) {
        String[] array = txt.split(" ");
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < array.length; i++) {
            result.append(array[i]);
        }
        return result.toString();
    }
}
