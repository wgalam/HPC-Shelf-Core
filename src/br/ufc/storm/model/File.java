package br.ufc.storm.model;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class File {

	private String name;
	private String extension;
	private String folder;
	private String build_cfg;
	private String version;
	private String filetype;
	private String url;
	private String content;

	public File(String name, String extension, String folder, String build_cfg, String version, String filetype, String url, String content) {
		this.name = name;
		this.extension = extension;
		this.folder = folder;
		this.build_cfg = build_cfg;
		this.version = version;
		this.filetype = filetype;
		this.url = url;
		this.content = content;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public String getBuild_cfg() {
		return build_cfg;
	}

	public void setBuild_cfg(String build_cfg) {
		this.build_cfg = build_cfg;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getFiletype() {
		return filetype;
	}

	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
