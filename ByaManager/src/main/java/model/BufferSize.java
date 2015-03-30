/*
Copyright 2011-2015 Stefano Cappa

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package model;

/**
 *	Classe che memorizza la dimensione del buffer per fa si che sia facilmente accessibile dalle altre classi.
 */
public final class BufferSize {

	private static int bufferSize;
	
	public static int getBufferSize() {
		return bufferSize;
	}

	public static void setBufferSize(int bufferDimension) {
		bufferSize = bufferDimension;
	}

	private BufferSize() {}
}
