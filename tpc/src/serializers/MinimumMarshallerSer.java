package serializers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;

import org.mk300.marshal.minimum.MinimumMarshaller;
import org.mk300.marshal.minimum.io.OInputStream;
import org.mk300.marshal.minimum.io.OOutputStream;

import data.media.MediaContent;

public class MinimumMarshallerSer {

	public static void register(final TestGroups groups) {

		groups.media.add(JavaBuiltIn.mediaTransformer,
				new MinimumSerializer<MediaContent>("minimum-marshaller",
						MediaContent.class), new SerFeatures(SerFormat.BINARY,
						SerGraph.FLAT_TREE, SerClass.ZERO_KNOWLEDGE,
						"flat tree graph zero knowledge"));
	}

	private static final class MinimumSerializer<T> extends Serializer<T> {
		private final String name;
		private final Class<T> clz;

		public MinimumSerializer(final String name, final Class<T> clz) {
			this.name = name;
			this.clz = clz;
		}
		
		@Override
		public String getName() {
			return name;
		}

		@SuppressWarnings("unchecked")
		@Override
		public T deserialize(final byte[] array) throws Exception {
			return (T) MinimumMarshaller.unmarshal(array);
		}

		@Override
		public byte[] serialize(final T data) throws IOException {
			return MinimumMarshaller.marshal(data);
		}

		@SuppressWarnings("unused") 
		@Override
		public final void serializeItems(final T[] items, final OutputStream os)
				throws Exception {
			for (Object item : items) {
				MinimumMarshaller.marshal(item, os);
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public T[] deserializeItems(final InputStream in, final int numOfItems)
				throws Exception {
			T[] result = (T[]) Array.newInstance(clz, numOfItems);
			for (int i = 0; i < numOfItems; ++i) {
				result[i] = (T)MinimumMarshaller.unmarshal(in);
			}
			return result;
		}
	}
}
