# Copyright (C) 2018 Jens Rehsack <sno@netbsd.org>
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Linux Kernel for Wendy Board"
DESCRIPTION = "Linux Kernel for Wendy Board"
LICENSE = "GPL-2.0"

LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

LINUX_VERSION = "5.4.68"

# Pick up shared functions
inherit kernel

PROVIDES += "virtual/kernel"
DEPENDS += "lzop-native bc-native bootscript-${MACHINE}-${WANTED_ROOT_DEV}"
RDEPENDS_${KERNEL_PACKAGE_NAME}-image_append = "\
  bootscript-${MACHINE}-${WANTED_ROOT_DEV}"

REV="5d087e3578cf9cbd850a6f0a5c8b8169f22b5272"
SRCREPO="kernel/git/stable/linux-stable.git"
SRCBRANCH = "linux-5.4.y"
#SRCBRANCH = "master"

SRC_URI = "git://git.kernel.org/pub/scm/linux/${SRCREPO};branch=${SRCBRANCH};rev=${REV} \
	   file://0001-perf-cs-etm-Move-definition-of-traceid_list-global-v.patch \
	   file://defconfig \
          "

# patches for hp
COMPATIBLE_MACHINE = "(wendy)"

# Taken from meta-fsl-arm
LOCALVERSION = "+wendy"
SCMVERSION ??= "y"

S = "${WORKDIR}/git"

# We need to pass it as param since kernel might support more then one
# machine, with different entry points
KERNEL_EXTRA_ARGS += "LOADADDR=${UBOOT_ENTRYPOINT}"

kernel_conf_variable() {
	CONF_SED_SCRIPT="$CONF_SED_SCRIPT /CONFIG_$1[ =]/d;"
	if test "$2" = "n"
	then
		echo "# CONFIG_$1 is not set" >> ${B}/.config
	else
		echo "CONFIG_$1=$2" >> ${B}/.config
	fi
}

do_configure_prepend() {
	echo "" > ${B}/.config
	CONF_SED_SCRIPT=""

	kernel_conf_variable LOCALVERSION "\"${LOCALVERSION}\""
	kernel_conf_variable LOCALVERSION_AUTO y

	sed -e "${CONF_SED_SCRIPT}" < '${WORKDIR}/defconfig' >> '${B}/.config'

	if [ "${SCMVERSION}" = "y" ]; then
		# Add GIT revision to the local version
		head=`git --git-dir=${S}/.git rev-parse --verify --short HEAD 2> /dev/null`
		printf "%s%s" +g $head > ${S}/.scmversion
	fi
}
