FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}-${MACHINE}:"

SRC_URI += "\
	file://bootsettings.patch \
	file://config.patch \
	file://common-image.patch \
	"
