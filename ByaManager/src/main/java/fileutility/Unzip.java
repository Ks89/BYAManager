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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import org.apache.log4j.Logger;

public final class Unzip {
	private static final Logger LOGGER = Logger.getLogger(Unzip.class);

	private Unzip(){}

//	public static void extract(Path path, String zipName, boolean isDownloaded) {
//		//per specificare codifica dello zip usare questo:
//		// Map<String, String> env = new HashMap<>();
//		//	env.put("create", "false"); //non creare un zip
//		//	env.put("encoding", "ISO-8859-1");
//		// e poi URI uri = URI.create("jar:file:" path + File.seprator + zipName);
//		//FileSystem fs = FileSystems.newFileSystem(uri, env);
//
//		String downloaded;
//		if(isDownloaded) {
//			downloaded = "_downloaded.txt";
//		} else {
//			downloaded = ".txt";
//		}
//
//		Path zipFileName = path.resolve(zipName);
//		try (FileSystem zipFs = FileSystems.newFileSystem(zipFileName, null)) {
//
//			List<Path> ListaPath  = FileList.getFileList(zipFs.getPath("/"));
//			//printo listapath
//			for(Path path1 : ListaPath) {
//				System.out.println(path1.toString());
//				System.out.println(path.resolve(path1.getFileName().toString().replace(".txt", downloaded)));
//			}
//
//			for (Path filePath : ListaPath) {
//				Files.copy(filePath, path.resolve(filePath.getFileName().toString().replace(".txt", downloaded)));
//			}
//		} catch (IOException e) {
//			LOGGER.error("update() - IOException= " + e);
//		}
//	}

	public static void extract_with_zip4j (Path dataPath, String zipName, String destination, boolean isDownloaded) {
		Path sourceZip = dataPath.resolve(zipName);

		try {
			ZipFile zipFile = new ZipFile(sourceZip.toString());
			zipFile.extractAll(destination);
		} catch (ZipException e) {
			LOGGER.error("extract_with_zip4j() - ZipException= " + e);
		}
	}
	
	public static void moveFiles (Path extractedFolder, Path dataPath, boolean isDownloaded) {
		String downloaded;
		if(isDownloaded) {
			downloaded = "_downloaded.txt";
		} else {
			downloaded = ".txt";
		}
		
		List<Path> filePathList  = FileList.getFileList(extractedFolder);
		
		for (Path filePath : filePathList) {
			try {
				Files.copy(filePath, dataPath.resolve(filePath.getFileName().toString().replace(".txt", downloaded)));
			} catch (IOException e) {
				LOGGER.error("moveFiles() - IOException= " + e);
			} 
		}
	}
	
	public static void removeTempFilesAndListsFolder(Path dataPath, Path extractedFolder, String zipName) throws IOException {
		//cancello lo zip
		Files.delete(dataPath.resolve(zipName));
		//cancello contenuto cartella e poi la cartella stessa
		List<Path> filePathList  = FileList.getFileList(extractedFolder);
		
		for (Path filePath : filePathList) {
			try {
				Files.delete(filePath);
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}

		Files.delete(dataPath.resolve("lists"));
	}
	

}