class LoginActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loadingDialog: AlertDialog
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Initialize loading dialog
        initLoadingDialog()
        
        // Set up animated background
        val animationDrawable = binding.backgroundView.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(2000)
        animationDrawable.setExitFadeDuration(4000)
        animationDrawable.start()
        
        // Animate views
        animateViews()
        
        // Button click animation and login
        binding.loginButton.setOnClickListener {
            val scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.button_scale)
            it.startAnimation(scaleAnimation)
            buildLoginRequest()
        }

        // Handle signup click
        binding.signupLink.setOnClickListener {
            // Navigate to signup activity
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        // Handle forgot password click
        binding.forgotPassword.setOnClickListener {
            // Navigate to forgot password activity
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }
    
    private fun initLoadingDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_loading, null)
        builder.setView(dialogView)
        builder.setCancelable(false)
        loadingDialog = builder.create()
        loadingDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    private fun buildLoginRequest() {
        // Get input values
        val username = binding.usernameInput.text.toString().trim()
        val password = binding.passwordInput.text.toString().trim()

        // Validate inputs
        if (!validateInputs(username, password)) {
            return
        }

        // Show loading dialog
        loadingDialog.show()

        // Create login request
        lifecycleScope.launch {
            try {
                val loginResult = performLogin(username, password)
                handleLoginResult(loginResult)
            } catch (e: Exception) {
                handleError(e)
            } finally {
                loadingDialog.dismiss()
            }
        }
    }

    private fun validateInputs(username: String, password: String): Boolean {
        var isValid = true

        // Username validation
        if (username.isEmpty()) {
            binding.usernameLayout.error = "Username is required"
            isValid = false
        } else {
            binding.usernameLayout.error = null
        }

        // Password validation
        if (password.isEmpty()) {
            binding.passwordLayout.error = "Password is required"
            isValid = false
        } else if (password.length < 6) {
            binding.passwordLayout.error = "Password must be at least 6 characters"
            isValid = false
        } else {
            binding.passwordLayout.error = null
        }

        return isValid
    }

    private suspend fun performLogin(username: String, password: String): LoginResult {
        return withContext(Dispatchers.IO) {
            // Replace this with your actual API call
            delay(1500) // Simulate network delay
            LoginResult(
                success = true,
                userData = UserData(
                    id = "123",
                    username = username,
                    email = "$username@example.com"
                )
            )
        }
    }

    private fun handleLoginResult(result: LoginResult) {
        if (result.success) {
            // Save user data to SharedPreferences or DataStore
            saveUserData(result.userData)
            
            // Navigate to main activity
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            showError("Login failed. Please try again.")
        }
    }

    private fun handleError(error: Exception) {
        val errorMessage = when (error) {
            is IOException -> "Network error. Please check your connection."
            is TimeoutException -> "Request timed out. Please try again."
            else -> "An error occurred. Please try again."
        }
        showError(errorMessage)
    }

    private fun showError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
            .setBackgroundTint(ContextCompat.getColor(this, R.color.error_color))
            .show()
    }

    private fun saveUserData(userData: UserData) {
        val sharedPrefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        sharedPrefs.edit().apply {
            putString("user_id", userData.id)
            putString("username", userData.username)
            putString("email", userData.email)
            apply()
        }
    }

    private fun animateViews() {
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        
        binding.apply {
            logoImage.startAnimation(fadeIn)
            
            // Delay animations for other views
            usernameLayout.startAnimation(fadeIn.apply { 
                startOffset = 300 
            })
            passwordLayout.startAnimation(fadeIn.apply { 
                startOffset = 500 
            })
            loginButton.startAnimation(fadeIn.apply { 
                startOffset = 700 
            })
            forgotPassword.startAnimation(fadeIn.apply { 
                startOffset = 900 
            })
        }
    }
}

// Data classes for login
data class LoginResult(
    val success: Boolean,
    val userData: UserData? = null,
    val errorMessage: String? = null
)

data class UserData(
    val id: String,
    val username: String,
    val email: String
) 