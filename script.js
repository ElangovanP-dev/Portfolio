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
        document.querySelectorAll('.gs-reveal, .gs-skill-card, .gs-project, .metrics-block, .cert-card').forEach(el => {
            el.style.opacity = '1';
            el.style.visibility = 'visible';
        });
        // Instantly set metric number text to target
        document.querySelectorAll('.metric-number').forEach(metric => {
            const target = metric.getAttribute('data-target');
            metric.textContent = target || '0';
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
    const hideLoader = () => {
        if (loader && loader.style.display !== 'none') {
            loader.style.opacity = '0';
            setTimeout(() => {
                loader.style.display = 'none';
            }, 600);
        }
    };

    if (hasGsap) {
        const tl = gsap.timeline();
        setTimeout(() => {
            hideLoader();
            // Set visible initial states to avoid FOUC
            gsap.set([".hero-title", ".tagline", ".hero-subtitle", ".hero-desc", ".hero-buttons", ".social-icon", ".scroll-indicator"], { visibility: "visible" });
            
            // Start Hero Animations
            tl.fromTo(".hero-content .hero-title", 
                { opacity: 0, scale: 0.96 }, 
                { opacity: 1, scale: 1, duration: 0.6, ease: "power2.out" }
            )
            .fromTo([
                ".hero-content .tagline", 
                ".hero-content .hero-subtitle", 
                ".hero-content .hero-desc", 
                ".hero-content .hero-buttons"
            ], 
                { opacity: 0, y: 15 }, 
                { opacity: 1, y: 0, duration: 0.6, stagger: 0.2, ease: "power2.out" }, 
                "-=0.35"
            )
            .fromTo(".social-links .social-icon", 
                { opacity: 0, x: -10 }, 
                { opacity: 1, x: 0, duration: 0.5, stagger: 0.1, ease: "power2.out" }, 
                "-=0.15"
            )
            .fromTo(".scroll-indicator", 
                { opacity: 0 }, 
                { opacity: 0.5, duration: 0.6 }, 
                "-=0.2"
            );
        }, 500); // 500ms loader for instant smooth reveal
    } else {
        setTimeout(hideLoader, 300);
    }

    // Failsafe: hide loader after 1 second under all circumstances
    setTimeout(hideLoader, 1000);

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

        // 2. Technical Skills Matrix
        // Section Entry: Container fades in smoothly
        gsap.fromTo(".skills-categories-grid", 
            { opacity: 0 },
            { 
                opacity: 1, duration: 0.8, ease: "power2.out",
                scrollTrigger: { trigger: ".skills-categories-grid", start: "top 85%" }
            }
        );

        // Staggered cascade of cards and their pills inside a single scroll timeline per card
        document.querySelectorAll(".skills-category-card").forEach((card) => {
            const tl = gsap.timeline({
                scrollTrigger: {
                    trigger: card,
                    start: "top 85%",
                    toggleActions: "play none none none"
                }
            });
            
            tl.fromTo(card,
                { autoAlpha: 0, y: 20 },
                { autoAlpha: 1, y: 0, duration: 0.6, ease: "power2.out" }
            )
            .fromTo(card.querySelectorAll(".skill-badge"), 
                { opacity: 0, scale: 0.8 },
                { opacity: 1, scale: 1, duration: 0.2, stagger: 0.04, ease: "back.out(1.2)" },
                "-=0.4"
            );
        });

        // 3. Experience Timeline
        // Scroll-Linked spine drawing effect
        gsap.fromTo(".timeline-spine", 
            { scaleY: 0 },
            {
                scaleY: 1,
                ease: "none",
                scrollTrigger: {
                    trigger: ".timeline",
                    start: "top 70%",
                    end: "bottom 80%",
                    scrub: true
                }
            }
        );

        // Cards and bullet points slide-in & stagger within a single timeline per card
        document.querySelectorAll(".timeline-item").forEach((item) => {
            const tl = gsap.timeline({
                scrollTrigger: {
                    trigger: item,
                    start: "top 85%",
                    toggleActions: "play none none none"
                }
            });
            
            tl.fromTo(item, 
                { autoAlpha: 0, x: -15, y: 15 },
                { autoAlpha: 1, x: 0, y: 0, duration: 0.5, ease: "power2.out" }
            )
            .fromTo(item.querySelectorAll(".exp-highlights li"),
                { autoAlpha: 0 },
                { autoAlpha: 1, duration: 0.4, stagger: 0.1, ease: "power2.out" },
                "-=0.2"
            );
        });

        // 4. Featured Projects
        // Cards fade in and slide up
        gsap.fromTo(".gs-project", 
            { autoAlpha: 0, y: 25 },
            { 
                autoAlpha: 1, y: 0, duration: 0.6, stagger: 0.2, ease: "power3.out",
                scrollTrigger: { trigger: ".projects-grid", start: "top 80%" }
            }
        );

        // 5. Certifications & Achievements
        // Combine metrics panel slide-in and certification cascade
        const credsTl = gsap.timeline({
            scrollTrigger: {
                trigger: ".credentials-grid",
                start: "top 85%",
                toggleActions: "play none none none"
            }
        });
        
        credsTl.fromTo(".metrics-block", 
            { autoAlpha: 0, x: -40 },
            { autoAlpha: 1, x: 0, duration: 0.8, ease: "power2.out" }
        )
        .fromTo(".cert-card", 
            { autoAlpha: 0, y: 15 },
            { autoAlpha: 1, y: 0, duration: 0.3, stagger: 0.06, ease: "power2.out" },
            "-=0.6"
        );

        // Numerical count-up for LeetCode problems (100) and Certifications (7)
        document.querySelectorAll('.metric-number').forEach((metricNumber) => {
            const target = parseInt(metricNumber.getAttribute('data-target'), 10);
            const obj = { value: 0 };
            
            gsap.to(obj, {
                value: target,
                duration: 1.2,
                ease: "power2.out",
                scrollTrigger: {
                    trigger: ".credentials-grid",
                    start: "top 85%",
                    toggleActions: "play none none none"
                },
                onUpdate: () => {
                    metricNumber.textContent = Math.floor(obj.value);
                }
            });
        });


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

    // Dynamic Trending Cursor Follower Effect
    if (window.innerWidth > 768) {
        const follower = document.createElement('div');
        follower.className = 'cursor-follower';
        document.body.appendChild(follower);

        let posX = -100, posY = -100;
        let mouseX = -100, mouseY = -100;

        window.addEventListener('mousemove', (e) => {
            mouseX = e.clientX;
            mouseY = e.clientY;
        });

        function animateCursor() {
            posX += (mouseX - posX) * 0.2;
            posY += (mouseY - posY) * 0.2;
            follower.style.transform = `translate3d(${posX - 18}px, ${posY - 18}px, 0)`;
            requestAnimationFrame(animateCursor);
        }
        animateCursor();

        // Target buttons & interactive elements for trending cursor hover state
        const targetElements = document.querySelectorAll('.btn, .btn-primary, .btn-secondary, .submit-btn, .nav-item, .social-icon');
        targetElements.forEach(el => {
            el.addEventListener('mouseenter', () => follower.classList.add('is-hovering-btn'));
            el.addEventListener('mouseleave', () => follower.classList.remove('is-hovering-btn'));
        });
    }
});
