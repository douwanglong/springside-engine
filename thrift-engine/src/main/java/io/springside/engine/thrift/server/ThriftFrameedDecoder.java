package io.springside.engine.thrift.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 因为Thrift固定前4位为Frame长度，更简洁直接的写法代替 LengthFieldBasedFrameDecoder
 * 
 * @author calvin
 */
public class ThriftFrameedDecoder extends ByteToMessageDecoder {

	private int LENGTH_FIELD_LENGTH = 4;

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.readableBytes() < LENGTH_FIELD_LENGTH) {
			return;
		}

		in.markReaderIndex();
		int frameLength = in.readInt();

		if (in.readableBytes() < frameLength) {
			in.resetReaderIndex();
			return;
		}

		ByteBuf frame = in.readSlice(frameLength).retain();
		out.add(frame);
	}
}
