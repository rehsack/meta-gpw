# The disk layout (in blocks) used is:
#  blocks                           KiB
#     0x0 +-----------------------+    0x0
#         | MBR / GPT ...         |
#   0x100 +-----------------------+   0x80
#         | Primary SPL           |
#   0x200 +-----------------------+  0x100
#         | Secondary SPL         |
#   0x300 +-----------------------+  0x180
#         | Primary U-Boot (TPL)  |
#  0x1000 +-----------------------+ 0x0800
#         | /boot                 |
#  0x2800 +-----------------------+ 0x1400
#         | / (app)               |
#  0x3000 +-----------------------+ 0x1800
#         | / (update)            |
#  0x3000 +-----------------------+ 0x1800
#         | /data                 |
#  0x3000 +-----------------------+ 0x1800

UPDATABLE_BUNDLE_HOOKS = "appboot prepare_overlay uboot_setenv unique_deploy"

UPDATABLE_BUNDLE_HOOK_unique_deploy[type] = "inline"
UPDATABLE_BUNDLE_HOOK_unique_deploy[trigger] = "postverify"
UPDATABLE_BUNDLE_HOOK_unique_deploy[code] = "if test $(mount | grep "on / type" | sed -e "s/ on.*//" -e "s,^/dev/.*,/dev/,") != "/dev/"; then MNT_BASE=${TEMP_DIR}/flashimg/$(cat /sys/class/net/eth0/address | sed -e "s/://g"); fi"

UPDATABLE_BUNDLE_HOOK_appboot[type] = "inline"
UPDATABLE_BUNDLE_HOOK_appboot[trigger] = "postdeploy postupdate"
UPDATABLE_BUNDLE_HOOK_appboot[code] = "if test "${UPDATE_APP}" = Y; then mv ${MNT_BOOT}/${KERNEL_IMAGETYPE}-${MACHINE} ${MNT_BOOT}/${KERNEL_IMAGETYPE}; fi"

UPDATABLE_BUNDLE_HOOK_prepare_overlay[type] = "inline"
UPDATABLE_BUNDLE_HOOK_prepare_overlay[trigger] = "postdeploy"
UPDATABLE_BUNDLE_HOOK_prepare_overlay[code] = "mkdir -p ${MNT_DATA}/{${OVERLAY_SHADOWS}}"

UPDATABLE_BUNDLE_HOOK_uboot_setenv[type] = "inline"
UPDATABLE_BUNDLE_HOOK_uboot_setenv[code] = 'test "${INTERNAL_ROOT_DEV}" = "${WANTED_ROOT_DEV}" && fw_setenv sysimg_version ${DISTRO_VERSION}'

UPDATABLE_BUNDLE_SLOTS = "SPL TPL BOOTSCRIPT KERNEL DTB"

UPDATABLE_SLOT_SPL = "${PREFERRED_PROVIDER_virtual/bootloader}"
UPDATABLE_SLOT_SPL[type] = "boot"
UPDATABLE_SLOT_SPL[file] = "${SPL_BINARY}"
UPDATABLE_SLOT_SPL[dest] = "raw"
UPDATABLE_SLOT_SPL[offset] = "256;512"
UPDATABLE_SLOT_SPL[condition] = "require_update_uboot"
UPDATABLE_SLOT_SPL[hook] = "uboot_setenv"
UPDATABLE_SLOT_TPL = "${PREFERRED_PROVIDER_virtual/bootloader}"
UPDATABLE_SLOT_TPL[type] = "boot"
UPDATABLE_SLOT_TPL[file] = "u-boot-${MACHINE}.${UBOOT_SUFFIX}"
UPDATABLE_SLOT_TPL[dest] = "raw"
UPDATABLE_SLOT_TPL[offset] = "768"
UPDATABLE_SLOT_TPL[together] = "SPL"

UPDATABLE_PARTITION_BOOT[offset] = "1M"

UPDATABLE_SLOT_BOOTSCRIPT = "bootscript-${MACHINE}-${WANTED_ROOT_DEV}"
UPDATABLE_SLOT_BOOTSCRIPT[type] = "boot"
UPDATABLE_SLOT_BOOTSCRIPT[file] = "bootscript"
UPDATABLE_SLOT_BOOTSCRIPT[dest] = "/boot"
UPDATABLE_SLOT_BOOTSCRIPT[together] = "KERNEL"
UPDATABLE_SLOT_KERNEL = "${PREFERRED_PROVIDER_virtual/kernel}"
UPDATABLE_SLOT_KERNEL[type] = "boot"
UPDATABLE_SLOT_KERNEL[file] = "${KERNEL_IMAGETYPE}"
UPDATABLE_SLOT_KERNEL[rename] = "${KERNEL_IMAGETYPE}-${MACHINE}"
UPDATABLE_SLOT_KERNEL[dest] = "/boot"
UPDATABLE_SLOT_DTB = "${PREFERRED_PROVIDER_virtual/kernel}"
UPDATABLE_SLOT_DTB[type] = "boot"
UPDATABLE_SLOT_DTB[file] = "${KERNEL_DEVICETREE}"
UPDATABLE_SLOT_DTB[dest] = "/boot"
UPDATABLE_SLOT_DTB[together] = "KERNEL"

include conf/machine/include/updatable-container-default-spec.inc
