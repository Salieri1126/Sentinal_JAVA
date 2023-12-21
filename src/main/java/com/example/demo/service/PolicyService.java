package com.example.demo.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.demo.repository.policy.UpdatePolicyMapper;

/**
 * 정책 관리 서비스 클래스
 */
@Service
@EnableScheduling
public class PolicyService {

    @Autowired
    private UpdatePolicyMapper updatePolicyMapper;

    /**
     * 주어진 내용을 URL 인코딩하는 메서드
     *
     * @param content 인코딩할 내용
     * @return URL 인코딩된 문자열
     * @throws UnsupportedEncodingException 인코딩이 지원되지 않는 경우 발생
     */
    public String encodingContent(String content) throws UnsupportedEncodingException {
        return URLEncoder.encode(content, StandardCharsets.UTF_8.toString());
    }

    /**
     * 주어진 내용을 URL 디코딩하는 메서드
     *
     * @param content 디코딩할 내용
     * @return URL 디코딩된 문자열
     * @throws UnsupportedEncodingException 디코딩이 지원되지 않는 경우 발생
     */
    public String decodingContent(String content) throws UnsupportedEncodingException {
        return URLDecoder.decode(content, StandardCharsets.UTF_8.toString());
    }

    /**
     * 주기적으로 실행되는 메서드로, 정책 업데이트를 수행하여 활성화 상태를 업데이트합니다.
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void updateEnableStatus() {
        updatePolicyMapper.updateEnableStatus();
    }

    /**
     * 주어진 포트 범위를 "~"를 기준으로 분리하는 메서드
     *
     * @param portRange 포트 범위를 나타내는 문자열
     * @return 포트 범위를 나타내는 문자열 배열 [시작포트, 종료포트]
     */
    public String[] isSplit(String portRange) {
        return portRange.split("~");
    }

    /**
     * UDP 프로토콜을 사용하여 메시지를 특정 호스트와 포트로 전송하는 메서드
     */
    public void sendUDP() {
        final String RECEIVER_HOST = "192.168.1.14";
        final int RECEIVER_PORT = 9999;
        final String MESSAGE = "test";

        try {
            InetAddress receiverAddress = InetAddress.getByName(RECEIVER_HOST);
            try (DatagramSocket socket = new DatagramSocket()) {
                byte[] buffer = MESSAGE.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, receiverAddress, RECEIVER_PORT);
                socket.send(packet);
            }
        } catch (UnknownHostException e) {
            System.err.println("호스트를 찾을 수 없습니다: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("I/O 예외가 발생했습니다: " + e.getMessage());
        }
    }
}
