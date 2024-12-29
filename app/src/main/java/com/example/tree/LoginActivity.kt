class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loadingDialog: AlertDialog
    private lateinit var backgroundAnimation: AnimationDrawable
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Apply enter transition
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        window.enterTransition = Explode()
        window.exitTransition = Explode()
        
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize shimmer
        shimmerFrameLayout = binding.shimmerLayout
        
        // Initialize animations
        setupAnimations()
        
        // Initialize loading dialog
        initLoadingDialog()

        // Set click listeners
        setupClickListeners()
        
        // Start shimmer
        shimmerFrameLayout.startShimmer()
        
        // Load background image with blur
        loadBlurredBackground()
    }

    private fun loadBlurredBackground() {
        Glide.with(this)
            .load(R.drawable.login_bg)
            .transform(BlurTransformation(25, 3))
            .into(binding.backgroundImage)
    }

    private fun setupAnimations() {
        // Start background animation
        binding.root.background?.let {
            backgroundAnimation = it as AnimationDrawable
            backgroundAnimation.setEnterFadeDuration(2000)
            backgroundAnimation.setExitFadeDuration(4000)
        }

        // Animate views with custom animations
        binding.apply {
            // Logo animation
            logoAnimation.addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    // Start form animations after logo
                    startFormAnimations()
                }
                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
            
            // Start logo animation
            logoAnimation.playAnimation()
        }
    }

    private fun startFormAnimations() {
        binding.apply {
            // Slide up animation for login form
            val slideUp = AnimationUtils.loadAnimation(this@LoginActivity, R.anim.slide_up)
            loginCard.startAnimation(slideUp)
            
            // Slide down animation for social buttons
            val slideDown = AnimationUtils.loadAnimation(this@LoginActivity, R.anim.slide_down)
            socialButtonsLayout.startAnimation(slideDown)
        }
    }

    private fun setupClickListeners() {
        binding.apply {
            loginButton.setOnClickListener {
                // Scale animation with ripple effect
                it.animate()
                    .scaleX(0.95f)
                    .scaleY(0.95f)
                    .setDuration(100)
                    .withEndAction {
                        it.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100)
                            .withEndAction {
                                buildLoginRequest()
                            }
                    }
                    .start()
            }

            forgotPassword.setOnClickListener {
                // Fade out and slide animation
                it.animate()
                    .alpha(0.5f)
                    .setDuration(100)
                    .withEndAction {
                        it.animate().alpha(1f).setDuration(100)
                        startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java),
                            ActivityOptions.makeSceneTransitionAnimation(this@LoginActivity).toBundle())
                    }
                    .start()
            }

            signupLink.setOnClickListener {
                // Fade out and slide animation
                it.animate()
                    .alpha(0.5f)
                    .setDuration(100)
                    .withEndAction {
                        it.animate().alpha(1f).setDuration(100)
                        startActivity(Intent(this@LoginActivity, SignUpActivity::class.java),
                            ActivityOptions.makeSceneTransitionAnimation(this@LoginActivity).toBundle())
                    }
                    .start()
            }
        }
    }

    private fun buildLoginRequest() {
        // Show loading with animation
        binding.loginButton.isEnabled = false
        binding.progressBar.visibility = View.VISIBLE
        binding.progressBar.animate()
            .alpha(1f)
            .setDuration(200)
            .start()

        // Get input values
        val username = binding.usernameInput.text.toString().trim()
        val password = binding.passwordInput.text.toString().trim()

        // Validate inputs
        if (!validateInputs(username, password)) {
            binding.loginButton.isEnabled = true
            binding.progressBar.animate()
                .alpha(0f)
                .setDuration(200)
                .withEndAction {
                    binding.progressBar.visibility = View.GONE
                }
                .start()
            return
        }

        // Perform login
        lifecycleScope.launch {
            try {
                val loginResult = performLogin(username, password)
                handleLoginResult(loginResult)
            } catch (e: Exception) {
                handleError(e)
            } finally {
                binding.loginButton.isEnabled = true
                binding.progressBar.animate()
                    .alpha(0f)
                    .setDuration(200)
                    .withEndAction {
                        binding.progressBar.visibility = View.GONE
                    }
                    .start()
            }
        }
    }

    // ... Rest of the existing code ...

    override fun onResume() {
        super.onResume()
        backgroundAnimation.start()
        shimmerFrameLayout.startShimmer()
    }

    override fun onPause() {
        super.onPause()
        backgroundAnimation.stop()
        shimmerFrameLayout.stopShimmer()
    }
} 