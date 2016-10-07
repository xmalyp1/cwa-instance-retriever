package sk.stuba.fei.dp.maly.ui;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class OwlFileFilter extends FileFilter{

	public static final String OWL_EXTENSION = "owl";
	
	@Override
	public String getDescription() {
		return "Only OWL files";
	}
	
	@Override
	public boolean accept(File f) {
		
		if(f.isDirectory())
			return true;
		
		return isOwlFile(f);
	}


	private static String getExtension(File f){
		int i = f.getName().lastIndexOf('.');
		if (i >= 0 && i+1 < f.getName().length()) {
			return f.getName().substring(i+1);
		}else{
			return "";
		}
		
	}
	
	public static boolean isOwlFile(File f){
		return getExtension(f).equals(OWL_EXTENSION);
	}
}


