window.showProfile = async function () {
  document.getElementById("feed-section").classList.add("hidden");
  document.getElementById("post-detail-section").classList.add("hidden");
  document.getElementById("profile-section").classList.remove("hidden");
};
// Función global para abrir post
window.openPost = function(post) {
  alert(post.title);
};
console.log("APP JS CARGADO");

// cargar supabase desde CDN global
const script = document.createElement("script");
script.src = "https://cdn.jsdelivr.net/npm/@supabase/supabase-js@2";

script.onload = () => initApp();

script.onerror = () => {
  console.error("Error cargando Supabase");
};

document.head.appendChild(script);

function initApp() {
      document.getElementById("app-section").style.display = "none";
    window.openPost = function(post) {

      document.getElementById("feed-section").classList.add("hidden");
      document.getElementById("post-detail-section").classList.remove("hidden");

      const container = document.getElementById("post-detail");

      container.innerHTML = `
        <div style="
          max-width:600px;
          margin:auto;
          background:white;
          padding:20px;
          border-radius:12px;
        ">

          ${
            post.image_url
            ? `<img src="${post.image_url}" style="
                width:100%;
                border-radius:10px;
                margin-bottom:15px;
              ">`
            : ""
          }

          <h2>${post.title}</h2>

          <p style="line-height:1.6;">
            ${post.content}
          </p>

        </div>
      `;
    };
  const supabase = window.supabase.createClient(
    "https://xoujnidjnjjvdxqzqrnr.supabase.co",
    "sb_publishable_fakO9tN8Q4iV5xpf3Uozkg_y1VfST9b"
  );

  // 🔴 comprobar sesión al cargar
  supabase.auth.getSession().then(({ data }) => {
    if (data.session) {
      document.getElementById("login-section").classList.add("hidden");
      document.getElementById("app-section").classList.remove("hidden");
      loadPosts();
    } else {
      document.getElementById("login-section").classList.remove("hidden");
      document.getElementById("app-section").classList.add("hidden");
    }
  });

  // 🔐 LOGIN
  window.login = async function () {
    console.log("CLICK LOGIN");

    const email = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    const { data, error } = await supabase.auth.signInWithPassword({
      email,
      password
    });

    console.log("LOGIN RESULT:", data, error);

    if (error) {
      alert(error.message);
      return;
    }

    showApp();
    loadPosts();
  };

  // 📝 REGISTER
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

  // 🚪 LOGOUT
  window.logout = async function () {
    await supabase.auth.signOut();

    document.getElementById("login-section").classList.remove("hidden");
    document.getElementById("app-section").classList.add("hidden");

    console.log("LOGOUT");
  };

  // 🟢 CREAR POST
  window.createPost = async function () {
    console.log("CREAR POST");

    const title = document.getElementById("title").value;
    const content = document.getElementById("content").value;
    const imageFile = document.getElementById("image").files[0];

    console.log("Imagen seleccionada:", imageFile);

    const { data: userData } = await supabase.auth.getUser();

    let imageUrl = null;

    if (imageFile) {
      const fileName = Date.now() + "-" + imageFile.name;

      console.log("Subiendo imagen con nombre:", fileName);

      const { data: uploadData, error: uploadError } = await supabase.storage
        .from("posts")
        .upload(fileName, imageFile);

      if (uploadError) {
        console.error("ERROR SUBIDA:", uploadError);
        alert(uploadError.message);
        return;
      }

      const { data: publicUrlData } = supabase.storage
        .from("posts")
        .getPublicUrl(fileName);

      imageUrl = publicUrlData.publicUrl;

      console.log("URL IMAGEN:", imageUrl);
    }

    const { error } = await supabase.from("posts").insert([
      {
        title,
        content,
        image_url: imageUrl,
        user_id: userData.user.id
      }
    ]);

    if (error) {
      console.error(error);
      alert(error.message);
      return;
    }

    alert("Post creado");

    document.getElementById("title").value = "";
    document.getElementById("content").value = "";
    document.getElementById("image").value = "";

    loadPosts();
  };

  // 📄 CARGAR POSTS (FEED)
  async function loadPosts() {
    const { data, error } = await supabase
      .from("posts")
      .select("*")
      .order("created_at", { ascending: false });

    if (error) {
      console.error(error);
      return;
    }

    renderPosts(data);
  }

  // 👤 CARGAR POSTS DEL USUARIO (para perfil)
  async function loadMyPosts() {
    const { data: userData } = await supabase.auth.getUser();

    const { data, error } = await supabase
      .from("posts")
      .select("*")
      .eq("user_id", userData.user.id)
      .order("created_at", { ascending: false });

    if (error) {
      console.error(error);
      return;
    }

    renderPosts(data);
  }

  // 🎨 RENDER POSTS
  function renderPosts(posts) {
    const container = document.getElementById("posts");
    container.innerHTML = "";

    posts.forEach(post => {
      const div = document.createElement("div");
      div.innerHTML = `
        <div style="
          background:white;
          padding:15px;
          margin-bottom:15px;
          border-radius:12px;
          box-shadow:0 2px 8px rgba(0,0,0,0.08);
        ">

          ${
            post.image_url
            ? `<img src="${post.image_url}" style="
                width:100%;
                height:200px;
                object-fit:cover;
                border-radius:10px;
                margin-bottom:10px;
              ">`
            : ""
          }

          <h3 style="margin:0;">
            ${post.title}
          </h3>

          <p style="color:#555;">
            ${post.content.substring(0, 100)}...
          </p>

        </div>
      `;
      div.onclick = () => openPost(post);
      container.appendChild(div);
    });
  }

  // 🎯 UI helper
  function showApp() {
    document.getElementById("login-section").style.display = "none";
    document.getElementById("app-section").style.display = "block";
  }

  // 🔽 NAVEGACIÓN APP
  window.showFeed = function () {
    document.getElementById("feed-section").classList.remove("hidden");
    document.getElementById("post-detail-section").classList.add("hidden");
    document.getElementById("profile-section").classList.add("hidden");
    loadPosts();
  };

  window.showProfile = function () {
    document.getElementById("feed-section").classList.add("hidden");
    document.getElementById("profile-section").classList.remove("hidden");
    loadMyPosts();
  };

  // 👤 CARGAR POSTS DEL USUARIO (para perfil)
  async function loadMyPosts() {
    const { data: userData } = await supabase.auth.getUser();
    if (!userData || !userData.user) return;
    const { data, error } = await supabase
      .from("posts")
      .select("*")
      .eq("user_id", userData.user.id)
      .order("created_at", { ascending: false });
    if (error) {
      console.error(error);
      return;
    }
    const container = document.getElementById("profile-posts");
    if (!container) return;
    container.innerHTML = "";
    data.forEach(post => {
      const div = document.createElement("div");
      div.innerHTML = `
        <div style="
          background:white;
          padding:15px;
          margin-bottom:15px;
          border-radius:12px;
          box-shadow:0 2px 8px rgba(0,0,0,0.08);
        ">
          <h3 style="margin:0; font-size:18px;">
            ${post.title}
          </h3>
          <p style="margin:10px 0; line-height:1.5;">
            ${post.content}
          </p>
          <div style="
            font-size:12px;
            color:gray;
            display:flex;
            justify-content:space-between;
          ">
            <span>${new Date(post.created_at).toLocaleDateString()}</span>
            <span>Reunión de Arte</span>
          </div>
        </div>
      `;
      container.appendChild(div);
    });
  }
  // Registrar Service Worker
  if ("serviceWorker" in navigator) {
    navigator.serviceWorker.register("./service-worker.js")
      .then(() => console.log("Service Worker registrado"))
      .catch(err => console.error("SW error", err));
  }
}