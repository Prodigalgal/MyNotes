package main

import (
	"net"

	"go.uber.org/zap"
	"go.uber.org/zap/zapcore"
)

func main() {
	// 连接到本地rsyslog服务器
	conn, err := net.Dial("tcp", "192.168.100.139:514")
	if err != nil {
		panic(err)
	}
	defer conn.Close()

	// 创建一个自定义的日志输出器
	encoder := zapcore.NewJSONEncoder(zap.NewProductionEncoderConfig())
	writer := zapcore.AddSync(conn)
	core := zapcore.NewCore(encoder, writer, zap.DebugLevel)

	// 创建一个Zap记录器
	logger := zap.New(core)

	// 写入一条日志消息
	logger.Info("This is a Info message")
	logger.Error("This is a Error message")
	logger.Fatal("This is a Warn message")
	logger.Info("This is a Info message")
	logger.Error("This is a Error message")
	logger.Fatal("This is a Warn message")
	logger.Info("This is a Info message")
	logger.Error("This is a Error message")
	logger.Fatal("This is a Warn message")
	logger.Info("This is a Info message")
	logger.Error("This is a Error message")
	logger.Fatal("This is a Warn message")
	logger.Info("This is a Info message")
	logger.Error("This is a Error message")
	logger.Fatal("This is a Warn message")
	logger.Info("This is a Info message")
	logger.Error("This is a Error message")
	logger.Fatal("This is a Warn message")
	logger.Info("This is a Info message")
	logger.Error("This is a Error message")
	logger.Fatal("This is a Warn message")

}
