package com.app.fr.getmymask.ui.dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.fr.getmymask.R
import com.app.fr.getmymask.core.BaseActivity

import kotlinx.android.synthetic.main.view_dialog_default.*
import org.jetbrains.anko.toast

class BaseDialogFragment : androidx.fragment.app.DialogFragment() {

    private lateinit var listener: DialogFragmentListener
    lateinit var mContext: Context
    lateinit var dialogTitle: String
    var textOkButton: String? = null

    companion object {

        @JvmStatic
        fun createDialog(): BaseDialogFragment {
            val frag = BaseDialogFragment()
            return frag
        }
    }

    fun setListener(listener: DialogFragmentListener) {
        this.listener = listener
    }

    fun show(activity: BaseActivity, tag: String) {
        super.show(activity.supportFragmentManager, tag)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.BaseDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.view_dialog_default, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_title_dialog.text = dialogTitle

        if (textOkButton != null) {
            dialog_confirm.text = textOkButton
        } else {
            dialog_confirm.text = getString(R.string.ok)
        }

        dialog_confirm.setOnClickListener {
            if (et_number_mask.text.isNullOrEmpty() || et_number_mask.text.toString() == "0") {
                dismiss()
            } else {
                dismiss()
                if (::listener.isInitialized)
                    listener.onDoneClicked(this, et_number_mask.text.toString())
            }
        }
    }

}
