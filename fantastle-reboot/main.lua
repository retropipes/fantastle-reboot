-- libraries
local push = require "lib/push"
local Gamestate = require "lib/hump/gamestate"

-- assets
local assets = {
    images = {
        avatars = {},
        boss = {},
        effects = {},
        monsters = {},
        objects = {},
        ui = {}
    },
    music = {},
    sounds = {}
}

-- game states
local states = {
    loader = {}
}

function love.load()
    -- Set up screen
    local gameWidth, gameHeight = 1080, 720
    local windowWidth, windowHeight = love.window.getDesktopDimensions()
    push:setupScreen(gameWidth, gameHeight, windowWidth, windowHeight, {fullscreen = true, resizable = true, highdpi = true, pixelperfect = false, canvas = false, stretched = true})
    -- Set up joysticks
    local joysticks = love.joystick.getJoysticks()
    local joycount = love.joystick.getJoystickCount()
    if joycount >= 1 then
        joystick = joysticks[1]
    end
    -- Game state setup
    Gamestate.registerEvents()
    Gamestate.switch(states.loader)
end

function love.resize(w, h)
    return push:resize(w, h)
end

-- Loader state
function states.loader:init()
    -- Cache logo image
    assets.images.ui.logo = love.graphics.newImage("assets/images/ui/logo.png")
    -- Cache title screen music
    assets.music.title = love.audio.newSource("assets/music/title.ogg", "stream")
    -- Set music options
    assets.music.title:setLooping(true)
    -- Play title music
    love.audio.play(assets.music.title)
end

function states.loader:draw()
    push:start()
    love.graphics.draw(assets.images.ui.logo)
    love.graphics.print({{0, 0, 0, 1}, "Press ESCAPE to quit"}, 400, 340)
    if joystick then
        love.graphics.print({{0, 0, 0, 1}, "Joystick detected; button 9 also quits"}, 400, 360)
    end
    push:finish()
end

function states.loader:update(dt)
    if love.keyboard.isScancodeDown("escape") then
        love.audio.stop()
        love.event.quit(0)
    end
    if joystick then
        if joystick:isDown(9) then
            love.audio.stop()
            love.event.quit(0)
        end
    end
end