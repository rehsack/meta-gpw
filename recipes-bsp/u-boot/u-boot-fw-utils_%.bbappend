FILESEXTRAPATHS_prepend := "${THISDIR}/u-boot-${MACHINE}:"

SRC_URI += "\
	file://config.patch \
	file://common-image.patch \
	"
