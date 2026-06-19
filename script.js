window.addEventListener('load', () => {
    const loader = document.getElementById('loader');

    // 1. Check if GSAP is loaded (e.g. offline fallback)
    const hasGsap = typeof gsap !== 'undefined';

    if (hasGsap) {
        // Register GSAP ScrollTrigger
        gsap.registerPlugin(ScrollTrigger);
    } else {
        console.warn("GSAP is not loaded. Falling back to non-animated layout.");
        // Instantly reveal all GSAP-hidden elements
        document.querySelectorAll('.gs-reveal, .gs-skill, .gs-project, .gs-gallery').forEach(el => {
            el.style.opacity = '1';
            el.style.visibility = 'visible';
        });
        // Instantly set skill bar progress
        document.querySelectorAll('.gs-skill .progress').forEach(progress => {
            const targetWidth = progress.getAttribute('data-width');
            progress.style.width = targetWidth || '0%';
        });
    }

    // 2. Initial Particle setup
    if(window.particlesJS) {
        particlesJS("particles-js", {
            "particles": {
                "number": { "value": 60, "density": { "enable": true, "value_area": 800 } },
                "color": { "value": "#e0e0e0" },
                "shape": { "type": "circle" },
                "opacity": { "value": 0.4, "random": false },
                "size": { "value": 3, "random": true },
                "line_linked": {
                    "enable": true,
                    "distance": 150,
                    "color": "#e0e0e0",
                    "opacity": 0.2,
                    "width": 1
                },
                "move": {
                    "enable": true,
                    "speed": 1.5,
                    "direction": "none",
                    "random": false,
                    "straight": false,
                    "out_mode": "out",
                    "bounce": false,
                }
            },
            "interactivity": {
                "detect_on": "canvas",
                "events": {
                    "onhover": { "enable": true, "mode": "grab" },
                    "onclick": { "enable": false },
                    "resize": true
                },
                "modes": {
                    "grab": { "distance": 140, "line_linked": { "opacity": 0.6 } }
                }
            },
            "retina_detect": true
        });
    }

    // 3. Loader & Intro Animation Timeline
    if (hasGsap) {
        const tl = gsap.timeline();
        setTimeout(() => {
            if(loader) loader.style.opacity = '0';
            setTimeout(() => {
                if(loader) loader.style.display = 'none';
                
                // Start Hero Animations one by one AFTER fully loaded
                tl.from(".hero-content .tagline", { y: 20, duration: 0.8, ease: "power3.out" })
                  .from(".hero-content .hero-title", { y: 30, duration: 0.8, ease: "power3.out" }, "-=0.6")
                  .from(".hero-content .hero-subtitle", { y: 20, duration: 0.8, ease: "power3.out" }, "-=0.6")
                  .from(".hero-content .hero-desc", { y: 20, duration: 0.8, ease: "power3.out" }, "-=0.6")
                  .from(".hero-content .hero-buttons", { y: 20, duration: 0.8, ease: "power3.out" }, "-=0.6")
                  .from(".social-links .social-icon", { scale: 0, duration: 0.5, stagger: 0.1, ease: "back.out(1.7)" }, "-=0.4")
                  .from(".scroll-indicator", { opacity: 0, duration: 1 }, "-=0.2");
            }, 800);
        }, 2000); // 2 seconds loader
    } else {
        // Fallback: Fade out loader after 1 second if offline
        setTimeout(() => {
            if(loader) {
                loader.style.opacity = '0';
                setTimeout(() => {
                    loader.style.display = 'none';
                }, 800);
            }
        }, 1000);
    }

    // 4. Sparkle Trail Cursor / Touch Logic (only if GSAP is loaded)
    if (hasGsap) {
        const colors = ["#e5b324", "#ffd043", "#e6eee9", "#8b9991", "#a2e0c3"];
        let lastSparkleTime = 0;

        const createSparkle = (x, y) => {
            const sparkle = document.createElement("div");
            sparkle.className = "sparkle";
            const color = colors[Math.floor(Math.random() * colors.length)];
            
            sparkle.innerHTML = `<svg viewBox="0 0 24 24" fill="none" class="sparkle-svg" xmlns="http://www.w3.org/2000/svg"><path d="M12 0L14.59 9.41L24 12L14.59 14.59L12 24L9.41 14.59L0 12L9.41 9.41L12 0Z" fill="${color}"/></svg>`;
            
            document.body.appendChild(sparkle);
            
            const size = Math.random() * 18 + 10;
            gsap.set(sparkle, {
                x: x,
                y: y,
                width: size,
                height: size,
                xPercent: -50,
                yPercent: -50,
                opacity: 1,
                rotation: Math.random() * 90
            });
            
            const moveX = (Math.random() - 0.5) * 120;
            const moveY = (Math.random() - 0.5) * 120 + 20; 
            
            gsap.to(sparkle, {
                x: x + moveX,
                y: y + moveY,
                opacity: 0,
                scale: 0,
                rotation: "+=" + (Math.random() * 180 - 90),
                duration: 0.8 + Math.random() * 0.5,
                ease: "power2.out",
                onComplete: () => sparkle.remove()
            });
        };

        const handleInteraction = (e) => {
            const clientX = e.touches ? e.touches[0].clientX : e.clientX;
            const clientY = e.touches ? e.touches[0].clientY : e.clientY;
            const now = Date.now();
            
            if (now - lastSparkleTime > 15) { 
                createSparkle(clientX, clientY);
                if(Math.random() > 0.5) {
                    createSparkle(clientX + (Math.random()-0.5)*20, clientY + (Math.random()-0.5)*20);
                }
                lastSparkleTime = now;
            }
        };

        window.addEventListener('mousemove', handleInteraction);
        window.addEventListener('touchmove', handleInteraction);
        window.addEventListener('click', (e) => {
            for(let i = 0; i < 6; i++) {
                createSparkle(e.clientX || (e.touches && e.touches[0].clientX), e.clientY || (e.touches && e.touches[0].clientY));
            }
        });
    }

    // 5. GSAP ScrollReveal Animations (only if GSAP is loaded)
    if (hasGsap) {
        document.querySelectorAll('.gs-reveal').forEach((elem) => {
            gsap.fromTo(elem, 
                { autoAlpha: 0, y: 50 }, 
                {
                    autoAlpha: 1, 
                    y: 0, 
                    duration: 1, 
                    ease: "power3.out",
                    scrollTrigger: {
                        trigger: elem,
                        start: "top 85%",
                        toggleActions: "play none none reverse"
                    }
                }
            );
        });

        // Animate Skills Bar Widths
        document.querySelectorAll('.gs-skill').forEach((elem) => {
            gsap.fromTo(elem, 
                { autoAlpha: 0, y: 40 }, 
                {
                    autoAlpha: 1, y: 0, duration: 0.8, ease: "power2.out",
                    scrollTrigger: { trigger: elem, start: "top 90%" }
                }
            );

            let progress = elem.querySelector('.progress');
            let targetWidth = progress.getAttribute('data-width');
            
            gsap.to(progress, {
                width: targetWidth,
                duration: 1.5,
                ease: "power4.out",
                scrollTrigger: {
                    trigger: elem,
                    start: "top 85%"
                }
            });
        });

        // Stagger Projects
        gsap.fromTo(".gs-project", 
            { autoAlpha: 0, y: 50 },
            { 
                autoAlpha: 1, y: 0, duration: 0.8, stagger: 0.2, ease: "power3.out",
                scrollTrigger: { trigger: ".projects-grid", start: "top 80%" }
            }
        );


    }

    // 6. Sticky Navbar & Back to Top logic
    const navbar = document.querySelector('.navbar');
    const backToTopBtn = document.getElementById('backToTop');

    if (navbar || backToTopBtn) {
        window.addEventListener('scroll', () => {
            if(window.scrollY > 50) {
                if(navbar) navbar.classList.add('scrolled');
            } else {
                if(navbar) navbar.classList.remove('scrolled');
            }

            if(backToTopBtn) {
                if(window.scrollY > 500) {
                    backToTopBtn.classList.add('active');
                } else {
                    backToTopBtn.classList.remove('active');
                }
            }
        });
    }

    if (backToTopBtn) {
        backToTopBtn.addEventListener('click', (e) => {
            e.preventDefault();
            window.scrollTo({ top: 0, behavior: 'smooth' });
        });
    }

    // 7. Mobile Menu Overlay, Dynamic Indicator & ScrollSpy
    const hamburger = document.getElementById('hamburgerBtn');
    const mobileOverlay = document.getElementById('mobileNavOverlay');
    const mobileNavLinks = document.querySelectorAll('.mobile-nav-link');
    const navLinks = document.querySelector('.nav-links');
    const navItems = document.querySelectorAll('.nav-item');
    const indicator = document.querySelector('.nav-indicator');

    // Toggle mobile nav overlay
    if (hamburger && mobileOverlay) {
        const closeMenu = () => {
            hamburger.classList.remove('active');
            mobileOverlay.classList.remove('active');
            document.body.style.overflow = '';
        };

        hamburger.addEventListener('click', () => {
            hamburger.classList.toggle('active');
            mobileOverlay.classList.toggle('active');
            document.body.style.overflow = mobileOverlay.classList.contains('active') ? 'hidden' : '';
        });

        // Back / Close button
        const closeBtn = document.getElementById('mobileNavClose');
        if (closeBtn) {
            closeBtn.addEventListener('click', closeMenu);
        }

        // Close overlay when a link is clicked
        mobileNavLinks.forEach(link => {
            link.addEventListener('click', (e) => {
                const targetId = link.getAttribute('href');
                if (targetId && targetId.startsWith('#')) {
                    e.preventDefault();
                    const targetElement = document.querySelector(targetId);
                    
                    // Close menu first
                    closeMenu();
                    
                    // Then scroll
                    if (targetElement) {
                        setTimeout(() => {
                            window.scrollTo({
                                top: targetElement.offsetTop - 80,
                                behavior: 'smooth'
                            });
                        }, 300);
                    }
                }

                // Update active state on mobile links
                mobileNavLinks.forEach(l => l.classList.remove('active'));
                link.classList.add('active');
            });
        });
    }

    // Nav Indicator Positioning function
    function positionIndicator(item) {
        if (!indicator || window.innerWidth <= 768) {
            if (indicator) indicator.style.opacity = '0';
            return;
        }
        indicator.style.left = `${item.offsetLeft}px`;
        indicator.style.width = `${item.offsetWidth}px`;
        indicator.style.top = `${item.offsetTop}px`;
        indicator.style.height = `${item.offsetHeight}px`;
        indicator.style.opacity = '1';
    }

    if (navItems.length > 0 && indicator) {
        navItems.forEach(item => {
            item.addEventListener('mouseenter', () => {
                positionIndicator(item);
            });

            item.addEventListener('click', (e) => {
                const targetId = item.getAttribute('href');
                if (targetId && targetId.startsWith('#')) {
                    e.preventDefault();
                    const targetElement = document.querySelector(targetId);
                    if (targetElement) {
                        window.scrollTo({
                            top: targetElement.offsetTop - 80,
                            behavior: 'smooth'
                        });
                        // Close mobile overlay if open
                        if (hamburger && mobileOverlay && mobileOverlay.classList.contains('active')) {
                            hamburger.classList.remove('active');
                            mobileOverlay.classList.remove('active');
                            document.body.style.overflow = '';
                        }
                    }
                }
                navItems.forEach(i => i.classList.remove('active'));
                item.classList.add('active');
                positionIndicator(item);
            });
        });

        // Reposition pill to active item when mouse leaves the navbar container
        if (navLinks) {
            navLinks.addEventListener('mouseleave', () => {
                const activeItem = document.querySelector('.nav-item.active');
                if (activeItem) {
                    positionIndicator(activeItem);
                } else {
                    indicator.style.opacity = '0';
                }
            });
        }

        // ScrollSpy logic to set active nav link based on scroll position
        const sections = document.querySelectorAll('section');
        window.addEventListener('scroll', () => {
            let currentSection = '';
            sections.forEach(section => {
                const sectionTop = section.offsetTop - 120;
                if (window.scrollY >= sectionTop) {
                    currentSection = section.getAttribute('id');
                }
            });

            navItems.forEach(item => {
                item.classList.remove('active');
                if (item.getAttribute('href') === `#${currentSection}`) {
                    item.classList.add('active');
                    positionIndicator(item);
                }
            });

            // Sync mobile nav links
            if (mobileNavLinks) {
                mobileNavLinks.forEach(link => {
                    link.classList.remove('active');
                    if (link.getAttribute('href') === `#${currentSection}`) {
                        link.classList.add('active');
                    }
                });
            }
        });

        // Initialize placement on load
        setTimeout(() => {
            const activeItem = document.querySelector('.nav-item.active');
            if (activeItem) {
                positionIndicator(activeItem);
            }
        }, 400);
    }

    // Contact Form Submission Handler
    const contactForm = document.querySelector('.contact-form');
    if (contactForm) {
        contactForm.addEventListener('submit', (e) => {
            e.preventDefault();
            
            const nameInput = document.getElementById('name');
            const emailInput = document.getElementById('email');
            const messageInput = document.getElementById('message');
            const submitBtn = contactForm.querySelector('.submit-btn');
            
            if (!nameInput || !emailInput || !messageInput || !submitBtn) return;
            
            // Save original button content and disable
            const originalBtnContent = submitBtn.innerHTML;
            submitBtn.disabled = true;
            submitBtn.innerHTML = 'Sending... <i class="fas fa-spinner fa-spin"></i>';
            
            fetch("https://formsubmit.co/ajax/elangovanp222@gmail.com", {
                method: "POST",
                headers: { 
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: JSON.stringify({
                    name: nameInput.value,
                    email: emailInput.value,
                    message: messageInput.value
                })
            })
            .then(response => {
                if (response.ok) {
                    return response.json();
                }
                throw new Error('Network response was not ok.');
            })
            .then(data => {
                showToast("Message sent successfully! I'll get back to you soon.", "success");
                contactForm.reset();
            })
            .catch(error => {
                console.error('Submission error:', error);
                showToast("Failed to send message. Please try again.", "error");
            })
            .finally(() => {
                submitBtn.disabled = false;
                submitBtn.innerHTML = originalBtnContent;
            });
        });
    }

    // Modern Toast Notification System
    function showToast(message, type) {
        const toast = document.createElement('div');
        toast.className = `toast ${type}`;
        toast.style.position = 'fixed';
        toast.style.bottom = '30px';
        toast.style.right = '30px';
        toast.style.padding = '15px 25px';
        toast.style.borderRadius = '8px';
        toast.style.color = '#fff';
        toast.style.fontSize = '0.95rem';
        toast.style.fontWeight = '500';
        toast.style.zIndex = '10000';
        toast.style.display = 'flex';
        toast.style.alignItems = 'center';
        toast.style.gap = '10px';
        toast.style.boxShadow = '0 10px 30px rgba(0,0,0,0.3)';
        toast.style.border = '1px solid rgba(255,255,255,0.1)';
        toast.style.transform = 'translateY(100px)';
        toast.style.opacity = '0';
        
        const icon = type === 'success' 
            ? '<i class="fas fa-check-circle" style="color: #ffd043;"></i>' 
            : '<i class="fas fa-exclamation-circle" style="color: #e71d36;"></i>';
            
        toast.innerHTML = `${icon} <span>${message}</span>`;
        
        if (type === 'success') {
            toast.style.background = 'rgba(8, 51, 32, 0.95)'; // Matching deep green
            toast.style.borderLeft = '4px solid #e5b324'; // Accent gold
        } else {
            toast.style.background = 'rgba(40, 10, 10, 0.95)';
            toast.style.borderLeft = '4px solid #e71d36';
        }
        
        document.body.appendChild(toast);
        
        // GSAP animation
        if (typeof gsap !== 'undefined') {
            gsap.to(toast, {
                y: 0,
                opacity: 1,
                duration: 0.5,
                ease: 'power3.out',
                onComplete: () => {
                    setTimeout(() => {
                        gsap.to(toast, {
                            y: 100,
                            opacity: 0,
                            duration: 0.5,
                            ease: 'power3.in',
                            onComplete: () => toast.remove()
                        });
                    }, 4000);
                }
            });
        } else {
            // CSS Fallback
            toast.style.transition = 'all 0.5s ease';
            setTimeout(() => {
                toast.style.transform = 'translateY(0)';
                toast.style.opacity = '1';
                setTimeout(() => {
                    toast.style.transform = 'translateY(100px)';
                    toast.style.opacity = '0';
                    setTimeout(() => toast.remove(), 500);
                }, 4000);
            }, 100);
        }
    }
});
