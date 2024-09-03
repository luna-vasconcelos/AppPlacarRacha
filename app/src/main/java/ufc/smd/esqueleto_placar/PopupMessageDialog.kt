import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import ufc.smd.esqueleto_placar.R


class PopupMessageDialog(private val message: String, private val onOkClicked: () -> Unit) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        // Inflate the custom layout
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.popup_message, null)

        // Set the view to the dialog
        builder.setView(view)
            .setCancelable(false)

        // Find the button in the inflated layout and set the click listener
        val okButton: Button = view.findViewById(R.id.btConfirmar)
        okButton.setOnClickListener {
            onOkClicked()
            dismiss() // This closes the dialog
        }

        // Create and return the dialog
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = (resources.displayMetrics.widthPixels * 0.8).toInt()
            val height = (resources.displayMetrics.heightPixels * 0.4).toInt()
            dialog.window?.setLayout(width, height)
        }
    }
}

