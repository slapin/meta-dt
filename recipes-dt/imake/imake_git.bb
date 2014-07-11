SRC_URI = "git://git.code.sf.net/p/cdesktopenv/code"
S = "${WORKDIR}/git/cde"
DEPENDS = "xproto"
LICENSE = "LGPLv2"
SRCREV = "master"
LIC_FILES_CHKSUM = "file://COPYING;md5=5cad1229d150b7e335f29ee73fb4fd39"
PARALLEL_MAKE = ""

do_configure() {
    : > config/cf/host.def
    echo "#define CcCmd \"${BUILD_CPP} -E" >>config/cf/host.def
    echo "#define CplusplusCmd \"${BUILD_CXX}\"" >>config/cf/host.def
}

R_FLAGS = "${CFLAGS} -I${STAGING_INCDIR}/usr/include/X11"

do_compile() {
	oe_runmake CC="${CC}" CFLAGS="${CFLAGS}" BOOTSTRAPCFLAGS="-DUSE_CC_E -DCC_PROGRAM=\"${TARGET_SYS}\"-gcc" -C config/imake -f Makefile.ini
	oe_runmake CC="${CC}" CFLAGS="${CFLAGS}" BOOTSTRAPCFLAGS="-DUSE_CC_E -DCC_PROGRAM=\"${TARGET_SYS}\"-gcc" Makefile
	oe_runmake CC="${CC}" CFLAGS="${CFLAGS}" BOOTSTRAPCFLAGS="-DUSE_CC_E -DCC_PROGRAM=\"${TARGET_SYS}\"-gcc" Makefile.boot
	oe_runmake CC="${CC}" CFLAGS="${CFLAGS}" BOOTSTRAPCFLAGS="-DUSE_CC_E -DCC_PROGRAM=\"${TARGET_SYS}\"-gcc" -C config/makedepend -f Makefile.proto makedepend
}
do_compile_class-cross() {
	oe_runmake CC="${CC}" CFLAGS="${CFLAGS} -I/usr/include/X11" BOOTSTRAPCFLAGS="-DUSE_CC_E -DCC_PROGRAM=\"${TARGET_SYS}\"-gcc" -C config/imake -f Makefile.ini imake
	unset CFLAGS
	oe_runmake CC="${CC}" BOOTSTRAPCFLAGS="-DUSE_CC_E -DCC_PROGRAM=\"${TARGET_SYS}\"-gcc" Makefile
	oe_runmake CC="${CC}" BOOTSTRAPCFLAGS="-DUSE_CC_E -DCC_PROGRAM=\"${TARGET_SYS}\"-gcc" Makefile.boot
	oe_runmake CC="${CC}" BOOTSTRAPCFLAGS="-DUSE_CC_E -DCC_PROGRAM=\"${TARGET_SYS}\"-gcc" -C config/makedepend -f Makefile.proto makedepend
}

do_install() {
	install -d -m 755 ${D}${bindir}
	install -c -m 755 ${B}/config/imake/imake ${D}${bindir}
	install -c -m 755 ${B}/config/makedepend/makedepend ${D}${bindir}
}

do_install_class-cross() {
	install -d -m 755 ${D}${bindir}
	install -c -m 755 ${B}/config/imake/imake ${D}${bindir}
	install -c -m 755 ${B}/config/makedepend/makedepend ${D}${bindir}
}

BBCLASSEXTEND = "cross"

