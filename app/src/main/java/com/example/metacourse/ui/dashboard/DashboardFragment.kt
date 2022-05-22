package com.example.metacourse.ui.dashboard

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.metacourse.MainActivity
import com.example.metacourse.PreferenceManager
import com.example.metacourse.R
import com.example.metacourse.databinding.FragmentDashboardBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val choosePhoto =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            chooseImageForPhoto(result)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        getImageForPhoto()

        binding.apply {
            addPhotoBtn.setOnClickListener {
                val strAvatarPrompt = "Выберите фото для профиля"
                val pickPhoto = Intent(Intent.ACTION_PICK)
                pickPhoto.type = "image/*"
                choosePhoto.launch(Intent.createChooser(pickPhoto, strAvatarPrompt))
            }
            exitBtn.setOnClickListener {
                activity?.finish()
            }
        }

        val nameTextView: TextView = binding.fioString
        val emailTextView: TextView = binding.emailString
        dashboardViewModel.name.observe(viewLifecycleOwner) {
            nameTextView.text = it
        }
        dashboardViewModel.email.observe(viewLifecycleOwner) {
            emailTextView.text = it
        }
        return root
    }

    private fun chooseImageForPhoto(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            var selectedImageUri = result.data?.data

            try {
                val input = context?.contentResolver?.openInputStream(selectedImageUri!!)
                val bitmap = BitmapFactory.decodeStream(input)
                val bos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
                var fileName = ""
                selectedImageUri.let { uri ->
                    context?.contentResolver?.query(uri!!, null, null, null, null)
                }?.use { cursor ->
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    cursor.moveToFirst()
                    fileName = cursor.getString(nameIndex)
                }
                val file = File(context?.cacheDir?.absolutePath + "/" + fileName)
                val fos = FileOutputStream(file)
                fos.write(bos.toByteArray())
                fos.flush()
                fos.close()

                // Get file size
                val imageSizeKb = file.length().toInt() / 1024
                if (imageSizeKb >= (1024 * 100)) {
                    Toast.makeText(context, "Big file size", Toast.LENGTH_SHORT).show()
                }



                binding.profileImage.setImageBitmap(bitmap)


                var temp: String? = null
                val imageDrawable = binding.profileImage.drawable as BitmapDrawable?
                if (imageDrawable?.bitmap != null) {
                    val image = imageDrawable?.bitmap
                    val BAOS = ByteArrayOutputStream()
                    image.compress(Bitmap.CompressFormat.JPEG, 100, BAOS)
                    val imageInBytes = BAOS.toByteArray()
                    temp = Base64.encodeToString(imageInBytes, Base64.DEFAULT)
                    GlobalScope.launch {
                        PreferenceManager.updatePhoto(temp)
                    }
                }

            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }
    }


    fun getImageForPhoto() {
        GlobalScope.launch(Dispatchers.Main) {
            PreferenceManager.preferencesFlow.collect {
//                val bitmap = BitmapFactory.decodeStream(it[PreferenceManager.PreferencesKeys.PHOTO]?.toByteArray()?.inputStream())
                if (it[PreferenceManager.PreferencesKeys.PHOTO] != "") {
                    val imageBytes = Base64.decode(it[PreferenceManager.PreferencesKeys.PHOTO], 0)
                    val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

                        binding.profileImage.setImageBitmap(image)

                } else {

                        binding.profileImage.setImageResource(R.drawable.profile_image)

                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}