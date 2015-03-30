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


package fileutility;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import notification.Notification;

public final class FileList {
	private static final Logger LOGGER = Logger.getLogger(FileList.class);

	private FileList() {}
	
	public static List<Path> getFileList(Path folderPath) {
		List<Path> fileList = new ArrayList<Path>();

		System.out.println(folderPath.toString());
		
		try (
				DirectoryStream<Path> ds = Files.newDirectoryStream(folderPath)
				) {
			
			if(Files.isDirectory(folderPath)) {
				for (Path filePath : ds) {
					System.out.println(filePath.toString());
					fileList.add(filePath);
				}
			}
		} catch (IOException e) {
			Notification.showErrorOptionPane("errorFileList", "errorFileListTitle");
		}
		return fileList;
	}
	
	
	public static void deleteFilesInList(Path folderPath) {
		for(Path path : FileList.getFileList(folderPath)) {
			try {
				Files.delete(path);
			} catch (IOException e) {
				LOGGER.error("deleteFilesInList() - IOException= " + e);
			}
		}
	}

}
