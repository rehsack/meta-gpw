IMAGE_INSTALL += "\
	${@bb.utils.contains("EXTRA_IMAGE_FEATURES", "debug-tweaks", "nfs-utils", "", d)} \
"
