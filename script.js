window.addEventListener('load', () => {
    
    // Register GSAP ScrollTrigger
    gsap.registerPlugin(ScrollTrigger);

    // 1. Initial Particle setup
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

    // Loader & Intro Animation Timeline
    const loader = document.getElementById('loader');
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

    // 3. Sparkle Trail Cursor / Touch Logic
    const colors = ["#e5b324", "#ffd043", "#e6eee9", "#8b9991", "#a2e0c3"];
    let lastSparkleTime = 0;

    const createSparkle = (x, y) => {
        const sparkle = document.createElement("div");
        sparkle.className = "sparkle";
        const color = colors[Math.floor(Math.random() * colors.length)];
        
        // Four-point star SVG
        sparkle.innerHTML = `<svg viewBox="0 0 24 24" fill="none" class="sparkle-svg" xmlns="http://www.w3.org/2000/svg"><path d="M12 0L14.59 9.41L24 12L14.59 14.59L12 24L9.41 14.59L0 12L9.41 9.41L12 0Z" fill="${color}"/></svg>`;
        
        document.body.appendChild(sparkle);
        
        const size = Math.random() * 18 + 10; // Made stars slightly larger
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
        
        const moveX = (Math.random() - 0.5) * 120; // Spread them out further
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
        
        // Spawn rapidly
        if (now - lastSparkleTime > 15) { 
            createSparkle(clientX, clientY);
            // Spawn a random secondary star for that "more stars" dense effect
            if(Math.random() > 0.5) {
                createSparkle(clientX + (Math.random()-0.5)*20, clientY + (Math.random()-0.5)*20);
            }
            lastSparkleTime = now;
        }
    };

    window.addEventListener('mousemove', handleInteraction);
    window.addEventListener('touchmove', handleInteraction);
    window.addEventListener('click', (e) => {
        // Explode multiple stars on click/tap
        for(let i = 0; i < 6; i++) {
            createSparkle(e.clientX || (e.touches && e.touches[0].clientX), e.clientY || (e.touches && e.touches[0].clientY));
        }
    });

    // 4. GSAP ScrollReveal Animations
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
                    start: "top 85%", // when top of elem hits 85% of viewport
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

    // Stagger Gallery
    gsap.fromTo(".gs-gallery", 
        { autoAlpha: 0, scale: 0.9 },
        { 
            autoAlpha: 1, scale: 1, duration: 0.8, stagger: 0.15, ease: "power2.out",
            scrollTrigger: { trigger: ".gallery-grid", start: "top 80%" }
        }
    );

    // 5. Sticky Navbar & Back to Top logic
    const navbar = document.querySelector('.navbar');
    const backToTopBtn = document.getElementById('backToTop');

    window.addEventListener('scroll', () => {
        if(window.scrollY > 50) {
            navbar.classList.add('scrolled');
        } else {
            navbar.classList.remove('scrolled');
        }

        if(window.scrollY > 500) {
            backToTopBtn.classList.add('active');
        } else {
            backToTopBtn.classList.remove('active');
        }
    });

    backToTopBtn.addEventListener('click', (e) => {
        e.preventDefault();
        window.scrollTo({ top: 0, behavior: 'smooth' });
    });

    // 6. Mobile Menu & Navigation
    const hamburger = document.querySelector('.hamburger');
    const navLinks = document.querySelector('.nav-links');

    if(hamburger) {
        hamburger.addEventListener('click', () => {
            if(navLinks.style.display === 'flex') {
                navLinks.style.display = 'none';
            } else {
                navLinks.style.display = 'flex';
                navLinks.style.flexDirection = 'column';
                navLinks.style.position = 'absolute';
                navLinks.style.top = '100%';
                navLinks.style.left = '0';
                navLinks.style.width = '100%';
                navLinks.style.background = 'rgba(4, 31, 19, 0.95)';
                navLinks.style.padding = '20px';
                navLinks.style.backdropFilter = 'blur(10px)';
                navLinks.style.borderBottom = 'var(--border-glass)';
            }
        });
    }

    // Smooth Scroll Navbar Area
    document.querySelectorAll('.nav-item').forEach(item => {
        item.addEventListener('click', (e) => {
            const targetId = item.getAttribute('href');
            if (targetId.startsWith('#')) {
                e.preventDefault();
                const targetElement = document.querySelector(targetId);
                if (targetElement) {
                    window.scrollTo({
                        top: targetElement.offsetTop - 80,
                        behavior: 'smooth'
                    });
                    if(window.innerWidth <= 768 && navLinks.style.display === 'flex') {
                        navLinks.style.display = 'none';
                    }
                }
            }
        });
    });
});
