package io.sarl.intellij

import com.intellij.openapi.util.IconLoader

import javax.swing.*

object SarlIcons {

    val SARL_PLUGIN = load("/icons/sarlIcon.png") // 256x256
    private fun load(path: String): Icon {
        return IconLoader.getIcon(path, SarlIcons::class.java)
    }
}
