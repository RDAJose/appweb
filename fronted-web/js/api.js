// 🔗 Conexión a Supabase

const SUPABASE_URL = "https://xoujnidjnjjvdxqzqrnr.supabase.co";
const SUPABASE_KEY = "sb_publishable_fakO9tN8Q4iV5xpf3Uozkg_y1VfST9b";

import { createClient } from "https://esm.sh/@supabase/supabase-js@2";

export const supabase = createClient(SUPABASE_URL, SUPABASE_KEY);