console.log("APP JS CARGADO");

// cargar supabase desde CDN global
const script = document.createElement("script");
script.src = "https://cdn.jsdelivr.net/npm/@supabase/supabase-js@2";
script.onload = () => initApp();
document.head.appendChild(script);

function initApp() {
  const supabase = window.supabase.createClient(
    "https://xoujnidjnjjvdxqzqrnr.supabase.co",
    "sb_publishable_fakO9tN8Q4iV5xpf3Uozkg_y1VfST9b"
  );

  window.login = async function () {
    console.log("CLICK LOGIN");

    const email = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    const { error } = await supabase.auth.signInWithPassword({
      email,
      password
    });

    if (error) {
      alert(error.message);
      return;
    }

    alert("Login correcto");
  };

  window.register = async function () {
    console.log("CLICK REGISTER");

    const email = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    const { error } = await supabase.auth.signUp({
      email,
      password
    });

    if (error) {
      alert(error.message);
      return;
    }

    alert("Usuario creado");
  };
}